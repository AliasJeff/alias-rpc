package com.alias.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock service proxy
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    /**
     * Process the method call of the proxy object
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Return the default value of the return type
        Class<?> methodReturnType = method.getReturnType();
        log.info("Mock invoke: {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * Get the default value of the return type
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type) {
        // Primitive type
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            } else if (type == float.class) {
                return 0.0F;
            } else if (type == double.class) {
                return 0.0D;
            } else if (type == char.class) {
                return "\u0000";
            }
        }
        // Object type
        return null;
    }
}
