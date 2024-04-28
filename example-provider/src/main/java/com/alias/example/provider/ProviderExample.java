package com.alias.example.provider;

import com.alias.RpcApplication;
import com.alias.example.common.service.UserService;
import com.alias.registry.LocalRegistry;
import com.alias.server.HttpServer;
import com.alias.server.VertxHttpServer;

public class ProviderExample {

    public static void main(String[] args) {

        RpcApplication.init();

        // register service
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // start web service
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8201);
    }
}
