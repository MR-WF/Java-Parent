package com.wf.netty.regiter;

/**
 * @description: 注册中心声明
 * @author: It.wf
 * @create: 15:46  2021/1/19
 **/
public interface RegisterCenter {
    /**
     * 基于服务名和服务地址注册一个服务
     * @param serviceName 服务名称
     * @param serviceAddr 服务地址
     */
    void register(String serviceName,String serviceAddr) throws Exception;
}
