package com.wf.netty.regiter;

import com.wf.netty.utils.ZkConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @description: 用zookeeper注册中心
 * @author: it.wf
 * @create: 2021-01-19 15:46
 **/
@Slf4j
public class ZkRegisterCenter implements RegisterCenter{
    // curator 对zookeeper的底层api的一些封装
    private CuratorFramework curatorFramework;

    public ZkRegisterCenter() {
        curatorFramework= CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.zkAddr)
                .sessionTimeoutMs(ZkConfig.timeout)
                .retryPolicy(new ExponentialBackoffRetry(100,10))
                .build();
        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String serviceAddr) throws Exception {
        log.info("注册中心开始register--{}--节点{}",serviceName,serviceAddr);

        String servicePath = ZkConfig.rootPath+"/"+serviceName;
        log.info("根目录{}",servicePath);
        if (curatorFramework.checkExists().forPath(servicePath) == null){
            log.info("进来了...............");
            curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());
            log.info("创建完了...............");
        }

       //注册服务，创建临时节点
        String addrPath = servicePath + "/" + serviceAddr;
        log.info("服务目录{}",addrPath);

        String rsNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                .forPath(addrPath,"0".getBytes());
        log.info("服务{}节点{}--->创建成功:{}",serviceName,serviceAddr,rsNode);

    }
}
