package com.alias.fault.tolerant;

import com.alias.model.RpcResponse;

import java.util.Map;

/**
 * Fail Fast Tolerant Strategy - instantly report error to the caller
 */
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("[Fail Fast] Service failed.", e);
    }
}
