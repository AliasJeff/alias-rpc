package com.alias.server;

import io.vertx.core.Vertx;

/**
 * Vertx HTTP server implementation
 *
 * @author Jeffery
 */
public class VertxHttpServer implements HttpServer{

    @Override
    public void doStart(int port) {
        // Create a new Vertx instance
        Vertx vertx = Vertx.vertx();

        // Create a new HTTP server
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // Request handler
        server.requestHandler(new HttpServerHandler());

        // Start the server and listen on port 8201
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port: " + port);
            } else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });
    }
}
