package com.alias.rpc.springboot.starter.annotation;

import com.alias.constant.RpcConstant;
import com.alias.fault.retry.RetryStrategyKeys;
import com.alias.fault.tolerant.TolerantStrategyKeys;
import com.alias.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Consumer annotation (for service injection)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcReference {

    Class<?> interfaceClass() default void.class;

    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    String retryStrategy() default RetryStrategyKeys.NO;

    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    boolean mock() default false;
}
