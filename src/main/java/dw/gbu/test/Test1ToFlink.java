package dw.gbu.test;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.sql.Connection;
import java.util.Properties;

public class Test1ToFlink {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //new 一个实例!
        Properties properties = new Properties();

        //告诉程序我们要接收那台机器上生产的数据
        properties.setProperty("bootstrap.servers", "slave002:9092,slave003:9092,slave004:9092,slave005:9092,slave006:9092");

        //告诉程序开启分区,已经分区名称
        properties.setProperty("group.id", "test_jf");

        //kafka分区自动发现周期
        properties.put(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, "3000");


        //告诉程序我需要开启一个偏移量的记录,并且是从头开始读的
        //properties.setProperty("auto.offset.reset", "earliest");

        //告诉kafka你不要自动提交偏移量了,每次搜自动提交偏移量
        //properties.setProperty("enable.auto.commit", "false");

        //如果FlinkKafkaConsumer没有开启checkpoint功能,为了不重复读取
        //这种方式无法实现Exactly-Once(只执行一次)
        //设置模式为：exactly_one，仅一次语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //确保检查点之间有1s的时间间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        //检查点必须在10s之内完成，或者被丢弃【checkpoint超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        //同一时间只允许进行一次检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        /*SimpleStringSchema可以获取到kafka消息*/
        //FlinkKafkaConsumer<String> flinkKafkaConsumer = new FlinkKafkaConsumer("mycanal-gbu-test1", new SimpleStringSchema(), properties);
        //DataStreamSource<String> lines = env.addSource(flinkKafkaConsumer);

        /*JSONKeyValueDeserializationSchema可以获取都消息的key,value，metadata:topic,partition，offset等信息*/
        FlinkKafkaConsumer011<ObjectNode> kafkaConsumer011 = new FlinkKafkaConsumer011<>("gbuvoucher-test1",new JSONKeyValueDeserializationSchema(true), properties);

        /** *
         如果checkpoint启⽤用，当checkpoint完成之后，Flink Kafka Consumer将会提交offset保存 到checkpoint State中，
         这就保证了了kafka broker中的committed offset与 checkpoint stata中的offset相⼀一致。 ⽤用户可以在Consumer中调⽤用setCommitOffsetsOnCheckpoints(boolean) ⽅方法来选择启⽤用 或者禁⽤用offset committing(默认情况下是启⽤用的)
         * */
        //kafkaConsumer011.setCommitOffsetsOnCheckpoints(true);
        //加入kafka数据源
        DataStreamSource<ObjectNode> streamSource = env.addSource(kafkaConsumer011);

        //数据传输到下游

        streamSource.addSink(new HbaseTwoPhaseCommitSink()).name("HbaseTwoPhaseCommitSink");




        env.execute("Test1ToFlink");

        System.out.println("=======================flink after=======================");
    }
}
