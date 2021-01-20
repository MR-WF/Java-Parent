package com.wf.dubbo2.demo;

import com.wf.dubbo2.demo.api.DemoService;
import com.wf.dubbo2.demo.provider.DemoServiceOne;
import com.wf.netty.regiter.RegisterCenter;
import com.wf.netty.regiter.ZkRegisterCenter;
import com.wf.netty.server.RpcServer;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-20 10:54
 **/
public class MyRpcServer {
    public static void main(String[] args) {
        List<Object> demoServices = new ArrayList<>();

        DemoService demoService2 = new DemoServiceOne();

        demoServices.add(demoService2);

        RegisterCenter registerCenter = new ZkRegisterCenter();
        RpcServer rpcServer = new RpcServer(registerCenter,"127.0.0.1:8089");
        rpcServer.bindService(demoServices);
        try {
            rpcServer.publisher();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
