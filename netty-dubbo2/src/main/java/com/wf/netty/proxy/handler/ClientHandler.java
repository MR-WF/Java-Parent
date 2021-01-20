package com.wf.netty.proxy.handler;

import com.alibaba.fastjson.JSON;
import com.wf.netty.vo.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 17:18
 **/
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.debug("收到response:{}",msg);
        RpcResponse response = JSON.parseObject(msg, RpcResponse.class);
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        ctx.channel().attr(key).set(response);
        ctx.channel().close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Unexpected exception from upstream.", cause);
        super.exceptionCaught(ctx, cause);
    }
}
