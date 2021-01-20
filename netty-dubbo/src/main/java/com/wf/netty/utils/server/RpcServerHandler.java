package com.wf.netty.utils.server;

import com.alibaba.fastjson.JSON;
import com.wf.netty.bean.ResponseCodeEnum;
import com.wf.netty.bean.RpcRequset;
import com.wf.netty.bean.RpcResponse;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 16:55
 **/
@Slf4j
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    private Map<String ,Object> handlerMap = new HashMap<>();

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequset  rpcRequset = (RpcRequset) msg;
        log.info("收到请求{}",rpcRequset.toString());
        Object result = null;
        if (handlerMap.containsKey(rpcRequset.getClassName())){
            /* Object clazz = handlerMap.get(rpcRequset.getClassName());
            Method method = clazz.getClass().getMethod(rpcRequset.getMethodName(),rpcRequset.getType());
           result = method.invoke(clazz,rpcRequset.getParams());
            ctx.write(result);
            ctx.flush();
            ctx.close();*/
            result = this.doInvoke(rpcRequset);
            ChannelFuture future = ctx.writeAndFlush(JSON.toJSONString(result));
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private Object doInvoke(RpcRequset  rpcRequset)  {
        //获得服务名称
        String serviceName = rpcRequset.getClassName();
        //获得版本号
        String version = rpcRequset.getVersion();
        //获得方法名
        String methodName = rpcRequset.getMethodName();
        //获得参数数组
        Object[] params = rpcRequset.getParams();
        //获得参数类型数据
        Class<?>[] argTypes = Arrays.stream(params).map(Object::getClass).toArray(Class<?>[]::new);
        /*if (version != null && !"".equals(version)) {
            serviceName = serviceName + "-" + version;
        }*/
        if (handlerMap.containsKey(rpcRequset.getClassName())){
            Object clazz = handlerMap.get(serviceName);
            Method method = null;
            try {
                method = clazz.getClass().getMethod(methodName,rpcRequset.getType());
                if (null == method) {
                    return RpcResponse.fail(ResponseCodeEnum.FILE, "未找到["+rpcRequset.getClassName()+"]方法");
                }
                return RpcResponse.success(method.invoke(clazz, params));
            } catch (Exception e) {
                e.printStackTrace();
                return RpcResponse.fail(ResponseCodeEnum.FILE, e.getMessage());
            }
        }else {
            return RpcResponse.fail(ResponseCodeEnum.FILE, "未找到["+rpcRequset.getClassName()+"]服务");
        }
    }
}
