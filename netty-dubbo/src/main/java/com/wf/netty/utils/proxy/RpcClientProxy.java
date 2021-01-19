package com.wf.netty.utils.proxy;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.regexp.internal.RE;
import com.wf.netty.bean.RpcRequset;
import com.wf.netty.service.ServiceDiscoveryService;
import com.wf.netty.utils.server.RpcServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.awt.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 17:03
 **/
public class RpcClientProxy {
    private ServiceDiscoveryService serviceDiscoveryService;

    public RpcClientProxy(ServiceDiscoveryService serviceDiscoveryService) {
        this.serviceDiscoveryService = serviceDiscoveryService;
    }

    public <T> T create(final Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequset rpcRequset = new RpcRequset();
                        rpcRequset.setClassName(method.getDeclaringClass().getName());
                        rpcRequset.setMethodName(method.getName());
                        rpcRequset.setType(method.getParameterTypes());
                        rpcRequset.setParams(args);

                        String serviceName = interfaceClass.getName();
                        String serviceAddress = serviceDiscoveryService.discover(serviceName);
                        String[] ipPorts = serviceAddress.split(":");
                        String ip = ipPorts[0];
                        int port =Integer.parseInt(ipPorts[1]);
                        final RpcProxyHandler rpcProxyHandler = new RpcProxyHandler();
                        EventLoopGroup group = new NioEventLoopGroup();
                        try {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(group).channel(NioSocketChannel.class)
                                .option(ChannelOption.TCP_NODELAY,true)
                                .handler(new ChannelInitializer<SocketChannel>() {
                                    @Override
                                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                                        ChannelPipeline pipeline = socketChannel.pipeline();
                                        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                                        pipeline.addLast("frameEncode",new LengthFieldPrepender(4));
                                        pipeline.addLast("encoder",new ObjectEncoder());
                                        pipeline.addLast("decode",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                        //实际IO操作Handler
                                        pipeline.addLast(rpcProxyHandler);
                                    }
                                });
                        ChannelFuture future = bootstrap.connect(ip,port).sync();
                        future.channel().writeAndFlush(rpcRequset);
                        future.channel().closeFuture().sync();
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            group.shutdownGracefully();
                        }
                        return rpcProxyHandler.getResponse();
                    }
                });

    }

}
