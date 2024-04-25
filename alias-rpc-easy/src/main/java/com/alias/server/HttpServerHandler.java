package com.alias.server;

import com.alias.model.RpcRequest;
import com.alias.model.RpcResponse;
import com.alias.registry.LocalRegistry;
import com.alias.serializer.JdkSerializer;
import com.alias.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Http server handler
 *
 * @author Jeffery
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {

        // Create a new Serializer instance
        final Serializer serializer = new JdkSerializer();

        // Record log
        System.out.println("Received request: " + request.method() + " " + request.uri());

        // Asynchronously handle the request
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Construct response
            RpcResponse rpcResponse = new RpcResponse();
            // If the request is invalid, return an error message
            if (rpcRequest == null) {
                rpcResponse.setMessage("Invalid request");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                // Get the class of the request, call by reflection
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                // Encapsulate the result into the response
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // Response
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * Response
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");

        try {
            // Serialize
            byte[] serializedData = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serializedData));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
