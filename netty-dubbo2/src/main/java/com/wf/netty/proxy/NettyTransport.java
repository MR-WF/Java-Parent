package com.wf.netty.proxy;

import com.alibaba.fastjson.JSON;
import com.wf.netty.proxy.handler.ClientHandler;
import com.wf.netty.vo.RpcRequset;
import com.wf.netty.vo.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 17:06
 **/
@Slf4j
public class NettyTransport {
    private String zkAddr;
    private static Bootstrap bootstrap;

    public NettyTransport(String zkAddr) {
        this.zkAddr = zkAddr;
    }

    static {

        //WORK线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // (1)
        //启动类
         bootstrap = new Bootstrap();
        bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast(new LengthFieldPrepender(4));
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new ClientHandler());
                    }
                }).option(ChannelOption.SO_KEEPALIVE, true);
    }


    public RpcResponse send(RpcRequset request) throws InterruptedException {
        log.info("zkAddr：{}",zkAddr);
        String[] split = zkAddr.split(":");
        log.info("zkAddr：{}", Arrays.asList(split));
        String serverIp = split[0];
        int serverPort = Integer.parseInt(split[1]);
        ChannelFuture channelFuture = bootstrap.connect(serverIp, serverPort).sync();
        Channel channel = channelFuture.channel();
        channel.writeAndFlush(JSON.toJSONString(request));
        //当通道关闭了，就继续往下走
        channelFuture.channel().closeFuture().sync();
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        return channel.attr(key).get();
    }

}
