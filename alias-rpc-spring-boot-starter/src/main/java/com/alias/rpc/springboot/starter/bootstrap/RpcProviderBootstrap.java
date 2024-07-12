package com.alias.rpc.springboot.starter.bootstrap;

import com.alias.RpcApplication;
import com.alias.config.RegistryConfig;
import com.alias.config.RpcConfig;
import com.alias.model.ServiceMetaInfo;
import com.alias.registry.LocalRegistry;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;
import com.alias.rpc.springboot.starter.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            log.info("RpcProviderBootstrap postProcessAfterInitialization...");

            Class<?> interfaceClass = rpcService.interfaceClass();

            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            LocalRegistry.register(serviceName, beanClass);

            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("Register service to registry error", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
