package com.alias.rpc.springboot.starter.annotation;

import com.alias.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provider annotation (for service registration)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    Class<?> interfaceClass() default void.class;

    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;
}
