package com.alias.config;

import com.alias.fault.retry.RetryStrategyKeys;
import com.alias.fault.tolerant.TolerantStrategyKeys;
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

    private Integer serverPort = 8801;

    private boolean mock = false;

    /**
     * Registry config
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    private String serializer = SerializerKeys.JDK;

    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    private String retryStrategy = RetryStrategyKeys.NO;

    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
