package com.alias.example.provider;

import com.alias.example.common.service.ApiService;
import com.alias.registry.LocalRegistry;
import com.alias.server.HttpServer;
import com.alias.server.VertxHttpServer;

/**
 * Easy provider example instance
 *
 * @author Jeffery
 */
public class EasyProviderExample {

    public static void main(String[] args) {

        // register service
        LocalRegistry.register(ApiService.class.getName(), ApiServiceImpl.class);

        // start web service
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8801);
    }
}
