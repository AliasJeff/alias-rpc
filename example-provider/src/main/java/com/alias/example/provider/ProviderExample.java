package com.alias.example.provider;

import com.alias.RpcApplication;
import com.alias.config.RegistryConfig;
import com.alias.config.RpcConfig;
import com.alias.example.common.service.UserService;
import com.alias.model.ServiceMetaInfo;
import com.alias.registry.LocalRegistry;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;
import com.alias.server.HttpServer;
import com.alias.server.VertxHttpServer;
import com.alias.server.tcp.VertxTcpServer;

public class ProviderExample {

    public static void main(String[] args) {

        RpcApplication.init();

        // register service
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // register service to registry
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // start web service
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStart(8801);
        // start TCP service
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(8801);
    }
}
