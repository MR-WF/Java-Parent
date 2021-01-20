package com.wf.netty;

import com.wf.netty.regiter.RegisterCenter;
import com.wf.netty.regiter.ZkRegisterCenter;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-20 10:30
 **/
public class TestRegister {
    public static void main(String[] args) {
        /*RegisterCenter registerCenter = new ZkRegisterCenter();
        try {
            registerCenter.register("com.wf.dubbo2.demo.api.DemoService","127.0.0.1:8080");
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        StringBuilder s = new StringBuilder("20210120162512131584");
        String sSysdate = s.toString();
        System.out.println(sSysdate);
        sSysdate = sSysdate.substring(0, 14);
        System.out.println(sSysdate);


    }
}
