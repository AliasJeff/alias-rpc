package com.alias.config;

import com.alias.serializer.SerializerKeys;
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

    private String serializer = SerializerKeys.JDK;
}
