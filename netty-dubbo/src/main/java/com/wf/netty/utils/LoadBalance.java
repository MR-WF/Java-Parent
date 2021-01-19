package com.wf.netty.utils;

import java.util.List;

/**
 * @description: 订阅 服务发现 负责均衡策略
 * @author: it.wf
 * @create: 2021-01-15 11:33
 **/
public interface LoadBalance {
    /**
     * 选取一个服务实例
     * @param resp
     * @return
     */
    String getOneServce(List<String> resp);
}
