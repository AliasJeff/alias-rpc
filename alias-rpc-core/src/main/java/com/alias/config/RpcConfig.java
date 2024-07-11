package com.alias.config;

import com.alias.loadbalancer.LoadBalancerKeys;
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

    private Integer serverPort = 8201;

    private boolean mock = false;

    /**
     * Registry config
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    private String serializer = SerializerKeys.JDK;

    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
}
