package com.alias.server.tcp;

import com.alias.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        // TODO: handle request
        return "Hello, client!".getBytes();
    }
    @Override
    public void doStart(int port) {
        // Create a Vertx instance
        Vertx vertx = Vertx.vertx();

        // Create a TCP server
        NetServer server = vertx.createNetServer();

        server.connectHandler(new TcpServerHandler());

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server started on port " + port);
            } else {
                System.err.println("Failed to start server on port " + port + ": " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
