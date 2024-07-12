package com.alias.bootstrap;

import com.alias.RpcApplication;
import com.alias.config.RegistryConfig;
import com.alias.config.RpcConfig;
import com.alias.model.ServiceMetaInfo;
import com.alias.model.ServiceRegisterInfo;
import com.alias.registry.LocalRegistry;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;
import com.alias.server.tcp.VertxTcpServer;

import java.util.List;

public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // Initialize the RpcApplication
        RpcApplication.init();
        // Global configuration
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // Register the service
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();

            // Register the service to the local registry
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            // Register the service to Etcd
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register the service to the registry", e);
            }
        }

        // Start the server
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
