package dw.gbu.jx;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

public class MySimpleStringSchema1 implements org.apache.flink.api.common.serialization.SerializationSchema<org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode> {
    @Override
    public byte[] serialize(ObjectNode jsonNodes) {
        return new byte[0];
    }
}
