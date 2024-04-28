package com.alias.server;

/**
 * HTTP server interface
 *
 * @author zhexun
 */
public interface HttpServer {

    /**
     * 启动服务器
     *
     * @param port 服务器端口
     */
    void doStart(int port);
}
