package dw.gbu.test;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaUtils {
    private static final String broker_list = "slave002:9092,slave003:9092,slave004:9092,slave005:9092,slave006:9092";
    //flink 读取kafka写入mysql exactly-once 的topic
    private static final String topic_ExactlyOnce = "hbase-exactly-once";

    public static void writeToKafka2() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 1; i <= 20; i++) {
                HbaseExactlyOncePOJO hbaseExactlyOnce = new HbaseExactlyOncePOJO(String.valueOf(i));
                ProducerRecord record = new ProducerRecord<String, String>(topic_ExactlyOnce, null, null, JSON.toJSONString(hbaseExactlyOnce));
                producer.send(record);
                System.out.println("发送数据: " + JSON.toJSONString(hbaseExactlyOnce));
                Thread.sleep(1000);
            }
        }catch (Exception e){

        }

        producer.flush();
    }

    public static void main(String[] args) throws InterruptedException {
        writeToKafka2();
    }

}
