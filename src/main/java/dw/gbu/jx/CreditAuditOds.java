package dw.gbu.jx;

import dw.gbu.init.CreditAudit;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.log4j.Logger;

import java.util.Properties;

public class CreditAuditOds {
    private static final org.apache.log4j.Logger log = Logger.getLogger(CreditAuditOds.class);
    private static final String topic_ods_credit_audit = "ods-t-ec-credit-audit";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度
        env.setParallelism(3);
        //checkpoint的设置
        //每隔10s进行启动一个检查点【设置checkpoint的周期】
        // 表示下 Cancel 时是否需要保留当前的 Checkpoint，默认 Checkpoint 会在整个作业 Cancel 时被删除。Checkpoint 是作业级别的保存点。
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 设置状态后端：MemoryStateBackend、FsStateBackend、RocksDBStateBackend，这里设置基于文件的状态后端
        //env.setStateBackend(new FsStateBackend("file:\\G\\flink\\checkpoints"));
        env.enableCheckpointing(10000);
        //设置模式为：exactly_one，仅一次语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //确保检查点之间有1s的时间间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        //检查点必须在10s之内完成，或者被丢弃【checkpoint超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        //同一时间只允许进行一次检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //表示一旦Flink程序被cancel后，会保留checkpoint数据，以便根据实际需要恢复到指定的checkpoint
        //env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //设置statebackend,将检查点保存在hdfs上面，默认保存在内存中。这里先保存到本地
        env.setStateBackend(new FsStateBackend("file:///g/flink/checkpoints/"));

        //设置kafka消费参数
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "slave002:9092,slave003:9092,slave004:9092,slave005:9092,slave006:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ods-credit-group1");
        //kafka分区自动发现周期
        props.put(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, "3000");

        /*SimpleStringSchema可以获取到kafka消息，JSONKeyValueDeserializationSchema可以获取都消息的key,value，metadata:topic,partition，offset等信息*/
        FlinkKafkaConsumer011<ObjectNode> kafkaConsumer011 = new FlinkKafkaConsumer011<>(topic_ods_credit_audit, new JSONKeyValueDeserializationSchema(true), props);

        //加入kafka数据源
        DataStreamSource<ObjectNode> streamSource = env.addSource(kafkaConsumer011);

        streamSource.print();

        //数据插入到ods层的hbase表
        streamSource.addSink(new OdsCreditAuditToHbase()).name("OdsCreditAuditToHbase");

        //数据流到汇总层的topic
        /**
        streamSource.addSink(new FlinkKafkaProducer011<ObjectNode>(
                "dws-credit-audit",
                (SerializationSchema<ObjectNode>) new CreditAuditOdsSchema(),
                props
        )).name("CreditAuditOdsKafka");
        **/
        //触发执行
        env.execute(CreditAuditOds.class.getName());
    }
}
