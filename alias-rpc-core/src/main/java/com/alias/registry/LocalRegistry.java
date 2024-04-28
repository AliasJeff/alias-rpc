package com.alias.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Local registry
 */
public class LocalRegistry {

    /**
     * Local registry map
     */
    public static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * Register service
     * @param interfaceName
     * @param clazz
     */
    public static void register(String interfaceName, Class<?> clazz) {
        map.put(interfaceName, clazz);
    }

    /**
     * Get service
     * @param interfaceName
     * @return
     */
    public static Class<?> get(String interfaceName) {
        return map.get(interfaceName);
    }

    /**
     * Remove service
     * @param interfaceName
     */
    public static void remove(String interfaceName) {
        map.remove(interfaceName);
    }
}
