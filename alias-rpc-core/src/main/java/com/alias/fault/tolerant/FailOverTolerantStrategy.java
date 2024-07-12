package com.alias.fault.tolerant;

import com.alias.model.RpcResponse;

import java.util.Map;

public class FailOverTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO implement fail over tolerant strategy
        return null;
    }
}
