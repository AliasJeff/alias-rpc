package com.alias;

import com.alias.config.RegistryConfig;
import com.alias.config.RpcConfig;
import com.alias.constant.RpcConstant;
import com.alias.registry.Registry;
import com.alias.registry.RegistryFactory;
import com.alias.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC framework application
 *
 * @author Jeffery
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * Init RPC framework, support custom configuration
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("Init RPC with config: {}", newRpcConfig.toString());

        // Init Registry
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("Init registry success, config: {}", registryConfig);

        // Add shutdown hook, destroy registry when JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * Init RPC framework with default configuration
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            log.error("Load default config error", e);
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * Get RPC configuration
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
