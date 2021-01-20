package com.wf.netty.utils;

import java.util.List;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 16:40
 **/
public abstract class BaseLoadBalance {

    public String selectServiceAddr(List<String> repos) {
        if (repos.isEmpty()) {
            return null;
        }
        if (repos.size() == 1) {
            return repos.get(0);
        }
        return getOneServiceAddr(repos);
    }

    protected abstract String getOneServiceAddr(List<String> repos);
}
