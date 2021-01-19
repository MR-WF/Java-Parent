package com.wf.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description:
 * @author: it.wf
 * @create: 2020-11-17 16:22
 **/
public class WebSocketNettyServer {

    public static void main(String[] args) {

        //主线程池
        NioEventLoopGroup mainGrp = new NioEventLoopGroup();
        //从线程池
        NioEventLoopGroup subGrp = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(mainGrp,subGrp)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketChannelInitiallizer());
            ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGrp.shutdownGracefully();
            subGrp.shutdownGracefully();
        }
    }
}
