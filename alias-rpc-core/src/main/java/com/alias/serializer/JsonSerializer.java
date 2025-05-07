package com.alias.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alias.model.RpcRequest;
import com.alias.model.RpcResponse;

import java.io.IOException;

/**
 * JSON Serializer
 *
 * @author Jeffery
 */
public class JsonSerializer implements Serializer{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, clazz);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest) obj, clazz);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse) obj, clazz);
        }
        return obj;
    }

    /**
     * Handle the request object
     * @param rpcRequest
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> clazz) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazzType = parameterTypes[i];
            if (!clazzType.isAssignableFrom(args[i].getClass())) {
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argBytes, clazzType);
            }
        }

        return clazz.cast(rpcRequest);
    }

    /**
     * Handle the response object
     * @param rpcResponse
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> clazz) throws IOException {
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataType()));
        return clazz.cast(rpcResponse);
    }
}
