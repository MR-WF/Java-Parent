package com.wf.netty.utils;

import java.util.List;
import java.util.Random;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 16:48
 **/
public class RandomLoadBalance extends BaseLoadBalance {
    @Override
    protected String getOneServiceAddr(List<String> resp) {
        return resp.get(new Random().nextInt(resp.size()));
    }
}
