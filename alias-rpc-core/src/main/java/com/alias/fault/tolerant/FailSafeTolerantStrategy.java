package com.alias.fault.tolerant;

import com.alias.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * Fail Safe Tolerant Strategy - ignore the error and return an empty response
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.error("[Fail Safe] Service failed.", e);
        return new RpcResponse();
    }
}
