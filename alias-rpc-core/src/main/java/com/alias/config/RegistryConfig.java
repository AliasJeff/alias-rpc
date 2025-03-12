package com.alias.config;

import lombok.Data;

@Data
public class RegistryConfig {

    /**
     * registry type
     */
    private String registry = "etcd";

    private String address = "http://localhost:2379";

    private String username;

    private String password;

    /**
     * Timeout (unit: millisecond)
     */
    private Long timeout = 10000L;
}
