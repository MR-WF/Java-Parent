package com.wf.netty.service.impl;

import com.wf.netty.service.ServiceDiscoveryService;
import com.wf.netty.utils.LoadBalance;
import com.wf.netty.utils.RandomLoadBalanceImpl;
import com.wf.netty.utils.curator.ZkConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 客户端服务发现
 * @author: it.wf
 * @create: 2021-01-15 10:55
 **/
public class ServiceDiscoveryServiceImpl implements ServiceDiscoveryService {

   private List<String> repos = new ArrayList<>();

   private CuratorFramework curatorFramework;

    public ServiceDiscoveryServiceImpl() {
        curatorFramework= CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.connectString)
                .sessionTimeoutMs(ZkConfig.timeout)
                .retryPolicy(new ExponentialBackoffRetry(100,10))
                .build();
        curatorFramework.start();
    }

    /**
     * 服务发现，拉取zk上的服务列表
     * @param serviceName
     * @return
     */
    @Override
    public String discover(String serviceName) {
        String path = ZkConfig.rootPath+"/"+serviceName;
        try {
            //拿到zk上该serviceName服务的所有的服务列表(地址信息)
            repos = curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通过zk的watch机制实现监听服务的
        registerWatch(path);

        //负载均衡算法、选取服务列表钟的一个服务调用
        LoadBalance loadBalance = new RandomLoadBalanceImpl();
        return loadBalance.getOneServce(repos);
    }

    /**
     * 利用zk的wartch机制监听 目录
     * @param path
     */
    private void registerWatch(String path) {
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> repos = curatorFramework.getChildren().forPath(path);
        childrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("cutomer监听pathChild Watch异常"+e.getMessage());
        }
    }
}
