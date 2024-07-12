package com.alias.rpc.springboot.starter.bootstrap;

import com.alias.RpcApplication;
import com.alias.config.RpcConfig;
import com.alias.rpc.springboot.starter.annotation.EnableRpc;
import com.alias.server.tcp.VertxTcpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("RpcInitBootstrap registerBeanDefinitions...");

        // Get the value of the @EnableRpc annotation
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");

        // Initialize the RPC framework
        RpcApplication.init();

        // Global configuration
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        if (needServer) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("needServer is false");
        }

    }
}
