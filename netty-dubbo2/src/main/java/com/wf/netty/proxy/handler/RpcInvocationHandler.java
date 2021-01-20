package com.wf.netty.proxy.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wf.netty.discover.ServerDiscover;
import com.wf.netty.proxy.NettyTransport;
import com.wf.netty.vo.ResponseCodeEnum;
import com.wf.netty.vo.RpcRequset;
import com.wf.netty.vo.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 17:45
 **/
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {
    private ServerDiscover serverDiscover;

    private String version;

    public RpcInvocationHandler(ServerDiscover serverDiscover, String version) {
        this.serverDiscover = serverDiscover;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequset rpcRequset = new RpcRequset();
        //服务名称
        rpcRequset.setServiceName(method.getDeclaringClass().getName());
        //方法名
        rpcRequset.setMethodName(method.getName());
        //参数类型
        rpcRequset.setType(method.getParameterTypes());
        //参数值
        rpcRequset.setParams(args);
        //版本
        rpcRequset.setVersion(version);

        String serviceName = method.getDeclaringClass().getName();
        if (null != version && !"".equals(version)) {
            serviceName += "-" + version;
        }
        String servicePath = serverDiscover.disvover(serviceName);
        log.info("servicePath:{}",servicePath);
        if (StringUtils.isEmpty(servicePath)) {
            log.error("未找到服务{}的注册信息...", serviceName);
            throw new RuntimeException("未找到服务["+serviceName+"]的注册信息");
        }
        RpcResponse response = new NettyTransport(servicePath).send(rpcRequset);
        if (response == null) {
            log.error("调用服务{}未收到响应...", serviceName);
            throw new RuntimeException("调用服务["+servicePath+"]未收到响应...");
        }
        log.info("调用服务{}远端实例{}响应{},RpcResponse:{}",serviceName, servicePath,
                JSONObject.toJSONString(JSON.toJSONString(response)));
        if (response.getCode() == null || !response.getCode().equals(ResponseCodeEnum.SUCCESS.getCode())) {
            log.error(serviceName+"远端服务调用失败:"+response.getMsg());
            throw new RuntimeException(serviceName+"远端服务调用失败:"+response.getMsg());
        } else {
            return response.getData();
        }

    }
}
