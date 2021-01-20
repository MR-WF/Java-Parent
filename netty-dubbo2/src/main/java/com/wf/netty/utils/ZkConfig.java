package com.wf.netty.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 15:49
 **/
@Configuration
public class ZkConfig {

    @Value("${zookeeper.address}")
    public  static   String zkAddr = "127.0.0.1:2181";

    @Value("${zookeeper.timeout}")
    public static  int timeout = 300;
    @Value("${zookeeper.root-path}")
    public static String rootPath = "/netty-dubbo";
}
