package com.rpc.provider.server;

import com.rpc.provider.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyRpcServer.java
 * @Description: Netty 服务端
 * 启动服务端监听端口
 * @CreateTime: 2022/7/25 14:46:00
 **/
@Component
public class NettyRpcServer implements DisposableBean {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    private final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    public void start(String host, int port) {
        try {
            // 1. 创建 bossGroup 和 workerGroup
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();

            // 2. 设置启动助手
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 3. 设置启动参数
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 添加 String 的编解码器
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());

                            // 添加自定义处理器
                             socketChannel.pipeline().addLast(nettyServerHandler);
                        }
                    });

            // 4. 绑定 ip 和端口号
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            logger.info("============= Netty 服务端启动 =============");
            // 5. 监听通道的关闭状态
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();

            // 关闭资源
            if (bossGroup != null)
                bossGroup.shutdownGracefully();
            if (workerGroup != null)
                workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void destroy() throws Exception {
        // 关闭资源
        if (bossGroup != null)
            bossGroup.shutdownGracefully();
        if (workerGroup != null)
            workerGroup.shutdownGracefully();
    }
}
