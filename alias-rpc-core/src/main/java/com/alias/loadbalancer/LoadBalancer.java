package com.alias.loadbalancer;

import com.alias.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * LoadBalancer (For consumer)
 */
public interface LoadBalancer {

    /**
     * Select service
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
