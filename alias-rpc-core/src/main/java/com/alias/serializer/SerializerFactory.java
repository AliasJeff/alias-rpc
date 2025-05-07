package com.alias.serializer;

import com.alias.spi.SpiLoader;


/**
 * Serializer Factory (Factory Pattern + Singleton Pattern)
 *
 * @author Jeffery
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * Get serializer instance by key
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
