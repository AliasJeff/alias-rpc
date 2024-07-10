package com.alias.protocol;

import com.alias.constant.ProtocolConstant;
import com.alias.enums.ProtocolMessageSerializerEnum;
import com.alias.enums.ProtocolMessageTypeEnum;
import com.alias.model.RpcRequest;
import com.alias.model.RpcResponse;
import com.alias.serializer.Serializer;
import com.alias.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;


public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);

        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new IOException("Invalid magic number: " + magic);
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        // Solve sticky packets
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());

        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new IOException("Invalid serializer: " + header.getSerializer());
        }

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageTypeEnum == null) {
            throw new IOException("Invalid message type: " + header.getType());
        }
        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest request = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEARTBEAT:
            case OTHERS:
            default:
                throw new RuntimeException("Invalid message type: " + header.getType());
        }
    }
}
