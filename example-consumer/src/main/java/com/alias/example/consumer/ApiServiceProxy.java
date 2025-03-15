package com.alias.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alias.example.common.model.User;
import com.alias.example.common.service.ApiService;
import com.alias.model.RpcRequest;
import com.alias.model.RpcResponse;
import com.alias.serializer.JdkSerializer;
import com.alias.serializer.Serializer;

import java.io.IOException;

/**
 * UserService static proxy
 */
public class ApiServiceProxy implements ApiService {

    @Override
    public User getUser(User user) {
        // Create a new serializer instance
        Serializer serializer = new JdkSerializer();

        // request
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(ApiService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8801")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
