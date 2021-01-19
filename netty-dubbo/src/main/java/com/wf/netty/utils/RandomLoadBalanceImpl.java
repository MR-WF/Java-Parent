package com.wf.netty.utils;

import java.util.List;
import java.util.Random;

/**
 * @description: 随机负载均衡策略
 * @author: it.wf
 * @create: 2021-01-15 11:36
 **/
public class RandomLoadBalanceImpl implements LoadBalance{
    @Override
    public String getOneServce(List<String> resp) {
        return resp.get(new Random().nextInt(resp.size()));
    }
}
