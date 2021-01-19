package com.wf.netty.utils.server;

import com.wf.netty.annotation.RpcService;
import com.wf.netty.service.MyService;
import com.wf.netty.service.RegisterCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 16:35
 **/
public class RpcServer {


    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private RegisterCenter registerCenter;
    private String serviceAddr;
    private Map<String,Object> handlerMap = new HashMap<>();

    public RpcServer(RegisterCenter registerCenter, String serviceAddr) {
        this.registerCenter = registerCenter;
        this.serviceAddr = serviceAddr;
    }

    public void publisher(){
        for(String serviceName : handlerMap.keySet()){
            registerCenter.register(serviceName,serviceAddr);
        }
        //BOSS线程组
        EventLoopGroup bossGrp = new NioEventLoopGroup();
        //WORK线程组
        EventLoopGroup workGrp = new NioEventLoopGroup();
        //启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGrp,workGrp);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //数据分包，组包，粘包
                pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                //拆包
                pipeline.addLast("frameEncode",new LengthFieldPrepender(4));
                pipeline.addLast("encoder",new ObjectEncoder());
                pipeline.addLast("decode",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                /**
                 p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                 p.addLast(new LengthFieldPrepender(4));
                 p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                 p.addLast(new StringEncoder(CharsetUtil.UTF_8));
                 **/
                //实际IO操作Handler
                pipeline.addLast(new RpcServerHandler(handlerMap));
            }
        }).option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);
        String[] addrs = serviceAddr.split(":");
        String ip = addrs[0];
        int port = Integer.parseInt(addrs[1]);
        ChannelFuture future = null;
        try {
            future = serverBootstrap.bind(ip,port).sync();
            logger.info("netty服务断启动成功，等待客户端连接:");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void bind(Object... objectsService) {
        for (Object service : objectsService){
            RpcService rpcService = service.getClass().getAnnotation(RpcService.class);
            if (rpcService == null){
                throw new RuntimeException("服务未找到注解..."+service.getClass());
            }
            String serviceName = rpcService.value().getName();
            String version = rpcService.version();
            if (!StringUtils.isEmpty(version)){
                //serviceName += version;
            }
            handlerMap.put(serviceName,service);
        }
    }
}
