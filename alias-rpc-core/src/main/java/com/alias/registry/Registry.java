package com.alias.registry;

import com.alias.config.RegistryConfig;
import com.alias.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {

    void init(RegistryConfig registryConfig);

    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    void destroy();
}
