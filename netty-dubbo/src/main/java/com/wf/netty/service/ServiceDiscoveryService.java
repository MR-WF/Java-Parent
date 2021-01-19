package com.wf.netty.service;

/**
 * @description: 服务发现
 * @author: it.wf
 * @create: 2021-01-15 10:53
 **/
public interface ServiceDiscoveryService {

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
