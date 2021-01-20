package com.wf.netty.proxy;

import com.wf.netty.discover.ServerDiscover;
import com.wf.netty.proxy.handler.RpcInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 17:41
 **/
public class RpcClientProxy {

    private ServerDiscover serverDiscover;

    public RpcClientProxy(ServerDiscover serverDiscover) {
        this.serverDiscover = serverDiscover;
    }

    public <T> T clientProxy(Class<T> interfaceCls) {
        return this.clientProxy(interfaceCls, null);
    }

    public <T> T clientProxy(Class<T> interfaceClass ,String version){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},new RpcInvocationHandler(serverDiscover,version));
    }

}
