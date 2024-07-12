package com.alias.example.provider;

import com.alias.RpcApplication;
import com.alias.bootstrap.ProviderBootstrap;
import com.alias.config.RegistryConfig;
import com.alias.config.RpcConfig;
import com.alias.example.common.service.UserService;
import com.alias.model.ServiceMetaInfo;
import com.alias.model.ServiceRegisterInfo;
import com.alias.registry.LocalRegistry;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;
import com.alias.server.HttpServer;
import com.alias.server.VertxHttpServer;
import com.alias.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

public class ProviderExample {

    public static void main(String[] args) {

        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<?> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
