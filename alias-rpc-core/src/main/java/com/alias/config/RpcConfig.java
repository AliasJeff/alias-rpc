package com.alias.config;

import lombok.Data;

/**
 * RPC config
 *
 * @author Jeffery
 */
@Data
public class RpcConfig {

    private String name = "alias-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private String serverPort = "8201";

    private boolean mock = false;
}
