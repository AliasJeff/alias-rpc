package com.alias.registry;

import com.alias.fault.retry.FixedIntervalRetryStrategy;
import com.alias.fault.retry.NoRetryStrategy;
import com.alias.fault.retry.RetryStrategy;
import com.alias.model.RpcResponse;
import org.junit.Test;

public class RetryStrategyTest {

//    RetryStrategy retryStrategy = new NoRetryStrategy();
    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                throw new Exception("test");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
