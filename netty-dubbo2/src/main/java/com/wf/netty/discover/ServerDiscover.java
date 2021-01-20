package com.wf.netty.discover;

/**
 * @description: 服务发现
 * @author: It.wf
 * @create: 16:15  2021/1/19
 **/
public interface ServerDiscover {
    /**
     * 基于服务名称获得一个服务提供者信息
     * @param serviceName 服务名称
     * @return 远端服务提供者信息
     */
    String disvover(String serviceName);
}
