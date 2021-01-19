package com.wf.netty.utils.curator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 10:49
 **/
@Configuration
public class ZkConfig {

    @Value("${zookeeper.address}")
    public  static   String connectString = "127.0.0.1:2181";

    @Value("${zookeeper.timeout}")
    public static  int timeout = 300;
    @Value("${zookeeper.root-path}")
    public static String rootPath = "/curator-test";
}
