package com.wf.netty.discover;

import com.wf.netty.regiter.ZkRegisterCenter;
import com.wf.netty.utils.BaseLoadBalance;
import com.wf.netty.utils.RandomLoadBalance;
import com.wf.netty.utils.ZkConfig;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 消费者-基于zookeeper的服务发现
 * @author: it.wf
 * @create: 2021-01-19 16:15
 **/
@Slf4j
public class ZkServerDiscover implements ServerDiscover {

    /**
     * 消费者订阅列表信息
     */
    Map<String, List<String>> repos = new ConcurrentHashMap<>();

    private CuratorFramework curatorFramework;
    BaseLoadBalance loadBalance;

    private String serverAddr;

    public ZkServerDiscover(String serverAddr,BaseLoadBalance loadBalance) {
        if (null == loadBalance){
            this.loadBalance = new RandomLoadBalance();
        }else {
            this.loadBalance = loadBalance;
        }
        this.serverAddr = serverAddr;
        curatorFramework= CuratorFrameworkFactory.builder()
                .connectString(serverAddr)
                .sessionTimeoutMs(30000)
                .retryPolicy(new ExponentialBackoffRetry(100,10))
                .build();
        curatorFramework.start();
    }


    /**
     * 消费者服务发现
     * @param serviceName 服务名称
     * @return
     */
    @Override
    public String disvover(String serviceName) {
        String path = ZkConfig.rootPath+"/"+serviceName;
        List<String> serviceAddresses;
        if (repos.containsKey(serviceName)){
            serviceAddresses = repos.get(serviceName);
        }else {
            try {
                serviceAddresses = curatorFramework.getChildren().forPath(path);
                repos.put(serviceName,serviceAddresses);
                //监听path目录，更新订阅列表信息
                registerWatcher(path,serviceName);
            } catch (Exception e) {
                log.error("获取zk目录{}异常{}",path, e.getMessage());
                throw new RuntimeException("获取zk目录["+path+"]异常：" + e.getMessage());
            }
        }
        //负载均衡算法、选取服务列表钟的一个服务调用
        return loadBalance.selectServiceAddr(serviceAddresses);
    }

    /**
     * 监听path目录，更新订阅列表信息
     * @param path
     * @param serviceName
     */
    private void registerWatcher(String path,String serviceName) {
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(path);
            repos.put(serviceName, serviceAddresses);
        };
        childrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            log.error("监听PatchChild Watcher异常{}", e.getMessage());
            throw new RuntimeException("监听PatchChild Watcher异常" + e.getMessage());
        }
    }
}
