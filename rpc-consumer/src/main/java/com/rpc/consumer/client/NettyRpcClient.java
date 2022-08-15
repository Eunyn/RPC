//package com.rpc.consumer.client;
//
//import com.rpc.consumer.handler.NettyRpcClientHandler;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.bytes.ByteArrayDecoder;
//import io.netty.handler.codec.bytes.ByteArrayEncoder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
///**
// * @Author: Eun
// * @Version 1.0.0
// * @ClassName: NettyRpcClient.java
// * @Description: Netty 客户端
// *                  1. 连接服务端
// *                  2. 关闭资源
// *                  3. 提供发送消息的方法
// * @CreateTime: 2022/7/25 16:29:00
// **/
////@Component
//public class NettyRpcClient implements InitializingBean, DisposableBean {
//
//    Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);
//
//    private EventLoopGroup group = null;
//    private Channel channel = null;
//
////    @Autowired
////    private NettyRpcClientHandler nettyRpcClientHandler;
//
//    // 创建线程池
//    ExecutorService service = Executors.newCachedThreadPool();
//
//    /**
//     * 1. 连接服务端
//     * @throws Exception
//     */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//        try {
//            // 1.1 创建线程组
//            group = new NioEventLoopGroup();
//            // 1.2 创建客户端启动助手
//            Bootstrap bootstrap = new Bootstrap();
//            // 1.3 设置参数
//            bootstrap.group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            // 添加编解码器
//                            socketChannel.pipeline().addLast(new ByteArrayDecoder());
//                            socketChannel.pipeline().addLast(new ByteArrayEncoder());
//
//                            // 添加自定义处理类
//                            socketChannel.pipeline().addLast(nettyRpcClientHandler);
//                        }
//                    });
//
//            // 1.4 连接服务
//            channel = bootstrap.connect("localhost", 8989).sync().channel();
//            logger.debug("==============客户端已连接==============");
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            // 关闭资源
//            if (channel != null)
//                channel.close();
//            if (group != null)
//                group.shutdownGracefully();
//        }
//    }
//
//
//    @Override
//    public void destroy() throws Exception {
//        // 关闭资源
//
//        if (channel != null)
//            channel.close();
//        if (group != null)
//            group.shutdownGracefully();
//    }
//
//    /**
//     * 消息发送
//     *
//     * @param msg 客户端请求
//     * @return  服务端响应
//     */
//    public byte[] send(byte[] msg) throws ExecutionException, InterruptedException {
//        nettyRpcClientHandler.setRequestMsg(msg);
//        logger.debug("执行 nettyRpcClientHandler");
//        Future submit = service.submit(nettyRpcClientHandler);
//        logger.debug("返回 服务端响应结果");
//        return (byte[]) submit.get();
//    }
//}
