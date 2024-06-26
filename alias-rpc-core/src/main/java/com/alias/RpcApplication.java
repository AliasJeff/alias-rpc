package com.alias;

import com.alias.config.RpcConfig;
import com.alias.constant.RpcConstant;
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
