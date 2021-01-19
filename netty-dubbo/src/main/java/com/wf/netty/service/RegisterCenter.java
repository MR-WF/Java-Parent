package com.wf.netty.service;

/**
 * 注册中心
 */
public interface RegisterCenter {
    /**
     * 服务注册
     * @param serviceName
     * @param serviceAddr
     */
    void register(String serviceName,String serviceAddr);

}
