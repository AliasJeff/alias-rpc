package com.alias.proxy;

import cn.hutool.core.collection.CollUtil;
import com.alias.RpcApplication;
import com.alias.config.RpcConfig;
import com.alias.constant.RpcConstant;
import com.alias.fault.retry.RetryStrategy;
import com.alias.fault.retry.RetryStrategyFactory;
import com.alias.fault.tolerant.TolerantStrategy;
import com.alias.fault.tolerant.TolerantStrategyFactory;
import com.alias.loadbalancer.LoadBalancer;
import com.alias.loadbalancer.LoadBalancerFactory;
import com.alias.model.RpcRequest;
import com.alias.model.RpcResponse;
import com.alias.model.ServiceMetaInfo;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alias.server.tcp.VertxTcpClient.doRequest;

/**
 * Service proxy (JDK dynamic proxy)
 *
 * @author Jeffery
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        String serviceName = method.getDeclaringClass().getName();

        // request
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        RpcResponse rpcResponse;
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

            // Retry Strategy, Send TCP request
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() -> doRequest(rpcRequest, selectedServiceMetaInfo));

        } catch (Exception e) {
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(RpcApplication.getRpcConfig().getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }

        return rpcResponse.getData();
    }
}
