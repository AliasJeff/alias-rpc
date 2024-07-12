package com.alias.fault.tolerant;

import com.alias.model.RpcResponse;

import java.util.Map;

public class FailBackTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO implement fail back tolerant strategy
        return null;
    }
}
