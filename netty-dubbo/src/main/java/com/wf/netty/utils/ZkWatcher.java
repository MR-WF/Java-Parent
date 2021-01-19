package com.wf.netty.utils;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-14 11:32
 **/
public class ZkWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZkWatcher.class);

    /**
     * 异步锁
     */
    private CountDownLatch countDownLatch;

    /**
     * 标记
     */
    private String mark;

    public ZkWatcher(CountDownLatch cd,String mark) {
        this.countDownLatch = cd;
        this.mark = mark;
    }

    public ZkWatcher() {
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //  三种监听类型： 创建，删除，更新
        logger.info("【Watcher监听事件】={}",watchedEvent.getState());
        logger.info("【监听路径为】={}",watchedEvent.getPath());
        logger.info("【监听的类型为】={}",watchedEvent.getType());
        if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
            //如果收到了服务端的响应事件,连接成功
            if (countDownLatch != null){
                countDownLatch.countDown();
            }
        }
    }
}
