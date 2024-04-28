package com.alias.example.consumer;

import com.alias.config.RpcConfig;
import com.alias.utils.ConfigUtils;

/**
 * @author Jeffery
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpcConfig);
    }
}
