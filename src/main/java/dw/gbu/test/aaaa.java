package dw.gbu.test;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

public class aaaa implements SerializationSchema<ObjectNode> {
    @Override
    public byte[] serialize(ObjectNode jsonNodes) {
        return new byte[0];
    }
}
