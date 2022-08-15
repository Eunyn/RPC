package com.rpc.remoting.transport.netty.server;

import com.rpc.config.RpcServiceConfig;
import com.rpc.provider.ServiceProvider;
import com.rpc.provider.impl.RedisServiceProviderImpl;
import com.rpc.utils.RuntimeUtil;
import com.rpc.utils.SingletonFactory;
import com.rpc.utils.threadpool.ThreadPoolFactoryUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyRpcServer.java
 * @Description: Netty 服务端
 * @CreateTime: 2022/8/5 15:43:00
 **/
@Component
public class NettyRpcServer {
    private final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    public static final int PORT = 8989;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private DefaultEventExecutorGroup serviceHandlerGroup ;


    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(RedisServiceProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            serviceHandlerGroup = new DefaultEventExecutorGroup(RuntimeUtil.cpus() * 2,
                    ThreadPoolFactoryUtil.createThreadFactory("server-handler-group", false));

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            // 添加 String 的编解码器
                            socketChannel.pipeline().addLast(new ByteArrayDecoder());
                            socketChannel.pipeline().addLast(new ByteArrayEncoder());

                            // 添加自定义处理器
                            socketChannel.pipeline().addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                        }
                    });

            // 绑定 ip 和端口号
            ChannelFuture channelFuture = bootstrap.bind(host, PORT).sync();
            logger.info("Netty 服务端启动, host: [{}], port: [{}]", host, PORT);
            // 等待服务端监听通道的关闭状态
            channelFuture.channel().closeFuture().sync();

        } catch (UnknownHostException | InterruptedException e) {
            logger.error("occur exception when start server: ", e);
        } finally {
            logger.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }

    }


}
