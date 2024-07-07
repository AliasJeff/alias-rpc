package com.alias.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alias.RpcApplication;
import com.alias.config.RpcConfig;
import com.alias.constant.RpcConstant;
import com.alias.model.RpcRequest;
import com.alias.model.RpcResponse;
import com.alias.model.ServiceMetaInfo;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;
import com.alias.serializer.JdkSerializer;
import com.alias.serializer.Serializer;
import com.alias.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Service proxy (JDK dynamic proxy)
 *
 * @author Jeffery
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String serviceName = method.getDeclaringClass().getName();
        // Create a new serializer instance
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // request
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("No service available");
            }
            // TODO Load balancing strategy
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            byte[] result;
            // TODO Address hard coded here, use the service discovery component to obtain the service address
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8201")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
