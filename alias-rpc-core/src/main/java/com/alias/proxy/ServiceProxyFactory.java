package com.alias.proxy;

import com.alias.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * Service proxy factory (create service proxy)
 *
 * @author Jeffery
 */
public class ServiceProxyFactory {

    /**
     * Create a service proxy instance based on the specified service class
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, new ServiceProxy());
    }

    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, new MockServiceProxy());
    }
}
