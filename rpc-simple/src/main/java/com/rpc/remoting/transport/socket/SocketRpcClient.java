package com.rpc.remoting.transport.socket;

import com.rpc.extension.ExtensionLoader;
import com.rpc.registry.ServiceDiscovery;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.transport.RpcRequestTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: SocketRpcClient.java
 * @Description: 客户端
 * @CreateTime: 2022/8/2 16:38:00
 **/
public class SocketRpcClient implements RpcRequestTransport {
    private final ServiceDiscovery serviceDiscovery;
    Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);

    public SocketRpcClient() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("redis");
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        logger.info("lookup Service: {}", inetSocketAddress);
        logger.info("client service: {}", rpcRequest.getClassName());
        try (Socket socket = new Socket()){
            socket.connect(inetSocketAddress);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send data to the server through the output stream
            objectOutputStream.writeObject(rpcRequest);

            // Read RpcResponse from the input stream
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        }catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("调用服务失败: ", e);
        }
    }
}
