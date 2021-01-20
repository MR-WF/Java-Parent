package com.wf.netty.server;

import com.wf.netty.annotations.RpcService;
import com.wf.netty.regiter.RegisterCenter;
import com.wf.netty.server.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 14:44
 **/
@Slf4j
public class RpcServer {
    private RegisterCenter registerCenter;
    /**
     * 服务发布IP:PORT
     */
    private String serviceAddr;
    //服务名称和服务对象的关系
    private Map<String,Object> handlerMap = new HashMap<>();

    public RpcServer(RegisterCenter registerCenter, String serviceAddr) {
        this.registerCenter = registerCenter;
        this.serviceAddr = serviceAddr;
    }

    /**
     * 发布服务
     */
    public void publisher() throws InterruptedException {
        //使用netty开启一个服务
        //BOSS线程组
        EventLoopGroup bossGrp = new NioEventLoopGroup();
        //WORK线程组
        EventLoopGroup workerGrp = new NioEventLoopGroup();
        //启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGrp,workerGrp).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        //数据分包，组包，粘包
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                        pipeline.addLast(new LengthFieldPrepender(4));
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new RpcServerHandler(handlerMap));
                    }
                }).option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);
        String[] addrs = serviceAddr.split(":");
        String serverIp = addrs[0];
        int serverPort = Integer.parseInt(addrs[1]);
        ChannelFuture future = null;
        try {
            future = bootstrap.bind(serverIp,serverPort).sync();
            log.info("服务[{}]启动成功，等待客户端连接......",serviceAddr);
            /*
             * 源码注释
             *  Wait until the server socket is closed，In this example, this does not happen, but you can do that to gracefully shut down your server.
             *
             *  future.channel().closeFuture().sync()
             * 如果缺失上述代码，则main方法所在的线程，即主线程会在执行完bind().sync()方法后，会进入finally 代码块，之前的启动的nettyserver也会随之关闭掉，整个程序都结束了；
             * future.channel().closeFuture().sync()目的是:
             * 让线程进入wait状态，也就是main线程暂时不会执行到finally里面，nettyserver也持续运行，如果监听到关闭事件，可以优雅的关闭通道和nettyserver，虽然这个例子中，永远不会监听到关闭事件。也就是说这个例子是仅仅为了展示存在api shutdownGracefully，可以优雅的关闭nettyserver。
             */
            //将服务注册到注册中心（集合中）
            addRegisters(serviceAddr);

            future.channel().closeFuture().sync();


        }catch (Exception e){
            e.printStackTrace();
        } finally {
            // 资源优雅释放
            bossGrp.shutdownGracefully();
            workerGrp.shutdownGracefully();
            if (future != null){
                future.channel().close();
            }
        }

    }

    /**
     * 服务提供者注册
     * @param serviceAddr
     */
    private void addRegisters(String serviceAddr) {
        log.info("进入方法.....addRegisters(String serviceAddr)");
        //服务注册
        handlerMap.keySet().forEach(serviceName -> {
            log.info("开始遍历集合......{}",serviceName);
            try {
                registerCenter.register(serviceName, serviceAddr);
            } catch (Exception e) {
                log.error("服务注册失败,e:{}", e.getMessage());
                throw new RuntimeException("服务注册失败");
            }
            log.info("成功注册服务，服务名称：{},服务地址：{}", serviceName, serviceAddr);
        });
    }



    /**
     * 绑定服务名以及服务对象
     *
     * @param services 服务列表
     */
    public void bindService(List<Object> services) {
        for (Object service : services) {
            RpcService anno = service.getClass().getAnnotation(RpcService.class);
            if (null == anno) {
                //注解为空的情况，version就是空，serviceName就是
                throw new RuntimeException("服务并没有注解请检查..." + service.getClass().getName());
            }
            String serviceName = anno.value().getName();
            String version = anno.version();
            if (!"".equals(version)) {
                serviceName += "-" + version;
            }
            log.info("将{}-{}添加到集合中" , serviceName , service.toString());
            handlerMap.put(serviceName, service);
        }
    }
}
