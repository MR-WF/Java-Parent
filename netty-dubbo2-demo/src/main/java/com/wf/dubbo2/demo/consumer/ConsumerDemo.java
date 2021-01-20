package com.wf.dubbo2.demo.consumer;

import com.wf.dubbo2.demo.api.DemoService;
import com.wf.netty.discover.ServerDiscover;
import com.wf.netty.discover.ZkServerDiscover;
import com.wf.netty.proxy.RpcClientProxy;
import com.wf.netty.utils.RandomLoadBalance;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 18:19
 **/
@Slf4j
public class ConsumerDemo {
    public static void main(String[] args) {
        ServerDiscover serverDiscover = new ZkServerDiscover("127.0.0.1:2181",new RandomLoadBalance());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serverDiscover);
        //测试服务版本
        try {
            DemoService ihello = rpcClientProxy.clientProxy(DemoService.class, "1.0");
            log.info(ihello.helloDubbo("我是客户端1"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用失败：e:{}",e.toString());
        }

        //测试集群
        /*for (int i = 0; i < 10; i++) {
            DemoService helloService = rpcClientProxy.clientProxy(DemoService.class);
            try {
                String result=helloService.helloDubbo("xxx");
                log.info("服务端影响 {}",result);
            } catch (Exception e) {
                log.error("调用失败：e:{}",e.toString());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

}
