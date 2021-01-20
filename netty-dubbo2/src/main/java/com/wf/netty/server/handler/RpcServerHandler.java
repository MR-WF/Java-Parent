package com.wf.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.wf.netty.vo.ResponseCodeEnum;
import com.wf.netty.vo.RpcRequset;
import com.wf.netty.vo.RpcResponse;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 14:52
 **/
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<String> {
    private Map<String ,Object> handlerMap = new HashMap<>();
    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //RpcRequset  rpcRequset = (RpcRequset) msg;
        RpcRequset rpcRequset = JSON.parseObject(msg, RpcRequset.class);

        log.info("服务拦截到请求{}",rpcRequset.toString());

        Object result = this.invoke(rpcRequset);
        ChannelFuture future = ctx.writeAndFlush(JSON.toJSONString(result));
        future.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     *  反射执行远程方法
     * @param rpcRequset
     * @return
     */
    private Object invoke(RpcRequset rpcRequset) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceName = rpcRequset.getServiceName();
        //获得参数类型数据
        //Class<?>[] argTypes = Arrays.stream(rpcRequset.getParams()).map(Object::getClass).toArray(Class<?>[]::new);
        if (!StringUtils.isEmpty(rpcRequset.getVersion())) {
            serviceName = serviceName + "-" + rpcRequset.getVersion();
        }

        //如果有该服务
        if (handlerMap.containsKey(serviceName)){
            Object service = handlerMap.get(serviceName);
           // Method method = service.getClass().getMethod(rpcRequset.getMethodName(), argTypes);
            Method method = service.getClass().getMethod(rpcRequset.getMethodName(), rpcRequset.getType());
            if (null == method) {
                return RpcResponse.fail(ResponseCodeEnum.FAIL, "未找到["+rpcRequset.getMethodName()+"]服务方法.....");
            }
            //利用反射method.invoke()方法，用来执行某个的对象的目标方法
            return RpcResponse.success(method.invoke(service,rpcRequset.getParams()));
        }else {
            return RpcResponse.fail(ResponseCodeEnum.FAIL,"未找到["+serviceName+"]服务.....");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
