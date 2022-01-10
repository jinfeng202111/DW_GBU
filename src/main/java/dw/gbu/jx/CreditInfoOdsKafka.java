package dw.gbu.jx;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

public class CreditInfoOdsKafka extends FlinkKafkaProducer011 {

    public CreditInfoOdsKafka(String brokerList, String topicId, SerializationSchema serializationSchema) {
        super(brokerList, topicId, serializationSchema);
    }
}
