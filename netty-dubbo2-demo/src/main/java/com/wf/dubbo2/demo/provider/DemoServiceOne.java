package com.wf.dubbo2.demo.provider;

import com.wf.dubbo2.demo.api.DemoService;
import com.wf.netty.annotations.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 18:14
 **/
@Slf4j
@RpcService(value = DemoService.class, version = "1.0")
public class DemoServiceOne implements DemoService {
    @Override
    public String helloDubbo(String args) {
        log.info("DemoServiceOne服务端收到请求："+args);
        return "DemoServiceOne服务端收到了你的请求: " + args;
    }
}
