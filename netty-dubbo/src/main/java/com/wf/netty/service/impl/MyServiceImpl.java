package com.wf.netty.service.impl;

import com.wf.netty.annotation.RpcService;
import com.wf.netty.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 17:27
 **/
@RpcService(MyService.class)
public class MyServiceImpl implements MyService {
    private static final Logger logger = LoggerFactory.getLogger(MyServiceImpl.class);
    @Override
    public String helloWord(String str) {
        logger.info("RPC调用成功啦...{}",str);
        return "你好["+str+"]服务端收到你的响应了";
    }
}
