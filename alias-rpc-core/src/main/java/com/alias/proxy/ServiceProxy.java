package com.alias.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alias.RpcApplication;
import com.alias.config.RpcConfig;
import com.alias.constant.ProtocolConstant;
import com.alias.constant.RpcConstant;
import com.alias.enums.ProtocolMessageSerializerEnum;
import com.alias.enums.ProtocolMessageTypeEnum;
import com.alias.loadbalancer.LoadBalancer;
import com.alias.loadbalancer.LoadBalancerFactory;
import com.alias.model.RpcRequest;
import com.alias.model.RpcResponse;
import com.alias.model.ServiceMetaInfo;
import com.alias.protocol.ProtocolMessage;
import com.alias.protocol.ProtocolMessageDecoder;
import com.alias.protocol.ProtocolMessageEncoder;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;
import com.alias.serializer.JdkSerializer;
import com.alias.serializer.Serializer;
import com.alias.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.alias.server.tcp.VertxTcpClient.doRequest;

/**
 * Service proxy (JDK dynamic proxy)
 *
 * @author Jeffery
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String serviceName = method.getDeclaringClass().getName();

        // request
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("No service available");
            }

            // Load balancing
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

            // Send TCP request
            RpcResponse rpcResponse = doRequest(rpcRequest, selectedServiceMetaInfo);
            return rpcResponse.getData();

        } catch (Exception e) {
            throw new RuntimeException("Failed to send request", e);
        }
    }
}
