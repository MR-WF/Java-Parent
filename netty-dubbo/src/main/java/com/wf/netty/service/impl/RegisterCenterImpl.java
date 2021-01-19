package com.wf.netty.service.impl;

import com.wf.netty.service.RegisterCenter;
import com.wf.netty.utils.curator.ZkConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 11:04
 **/
public class RegisterCenterImpl implements RegisterCenter {

    private static   final Logger logger = LoggerFactory.getLogger(RegisterCenterImpl.class);

    private CuratorFramework curatorFramework;

    public RegisterCenterImpl() {
        curatorFramework= CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.connectString)
                .sessionTimeoutMs(ZkConfig.timeout)
                .retryPolicy(new ExponentialBackoffRetry(100,10))
                .build();
        curatorFramework.start();
    }


    @Override
    public void register(String serviceName, String serviceAddr) {
        String servicePath = ZkConfig.rootPath+"/"+serviceName;
        try {
            if (curatorFramework.checkExists().forPath(servicePath) == null){
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());
            }
            String addrPath = servicePath + "/" + serviceAddr;
            String rsNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(addrPath,"0".getBytes());
            logger.info("服务{}--{}注册成功:{}",serviceName,serviceAddr,rsNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
