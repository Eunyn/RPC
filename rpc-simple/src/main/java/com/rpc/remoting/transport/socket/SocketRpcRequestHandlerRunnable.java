package com.rpc.remoting.transport.socket;

import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.dto.RpcResponse;
import com.rpc.remoting.handler.RpcRequestHandler;
import com.rpc.utils.SingletonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: SocketRpcRequestHandlerRunnable.java
 * @Description: 任务线程
 * @CreateTime: 2022/8/2 16:49:00
 **/
public class SocketRpcRequestHandlerRunnable implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(SocketRpcRequestHandlerRunnable.class);
    private Socket socket;
    private final RpcRequestHandler rpcRequestHandler;

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        logger.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        RpcResponse rpcResponse = new RpcResponse();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 读取客户端请求
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);

            rpcResponse.setResult(result);

            // 返回结果
            objectOutputStream.writeObject(rpcResponse);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            rpcResponse.setError(e.getMessage());
            logger.error("occur exception: ", e);
        }
    }
}
