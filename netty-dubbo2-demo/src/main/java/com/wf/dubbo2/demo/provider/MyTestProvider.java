package com.wf.dubbo2.demo.provider;

import com.wf.netty.regiter.RegisterCenter;
import com.wf.netty.regiter.ZkRegisterCenter;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 18:28
 **/
public class MyTestProvider {
    public static void main(String[] args) {
        RegisterCenter registerCenter = new ZkRegisterCenter();
        try {
            registerCenter.register("com.wf.dubbo2.demo.api.DemoService","127.0.0.1:8080");
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
