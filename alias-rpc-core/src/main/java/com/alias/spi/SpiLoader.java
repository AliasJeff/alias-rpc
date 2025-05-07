package com.alias.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.alias.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI Loader
 */
@Slf4j
public class SpiLoader {

    private static final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * Instance cache, avoid creating duplicate instances
     */
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * System SPI directory
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * Custom SPI directory
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * Scan directories
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_CUSTOM_SPI_DIR, RPC_SYSTEM_SPI_DIR};

    /**
     * Dynamic load SPI
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = List.of(Serializer.class);

    /**
     * Load all SPI
     */
    public static void loadAll() {
        log.info("Start loading all SPI...");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /**
     * Get instance by class and key
     * @param tClass
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException("No SPI implementation found for " + tClassName);
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException("No SPI implementation found for " + tClassName + " with key " + key);
        }
        Class<?> implClass = keyClassMap.get(key);

        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Failed to create instance of " + implClassName, e);
            } catch (InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return (T) instanceCache.get(implClassName);
    }

    /**
     * Load an SPI type
     * @param loadClass
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("Start loading SPI for {}", loadClass.getName());
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir: SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed to load SPI for {}", loadClass.getName(), e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }


}
