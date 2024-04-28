package com.alias.proxy;

import java.lang.reflect.Proxy;

/**
 * Service proxy factory (create service proxy)
 */
public class ServiceProxyFactory {

    /**
     * Create a service proxy instance based on the specified service class
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, new ServiceProxy());
    }
}
