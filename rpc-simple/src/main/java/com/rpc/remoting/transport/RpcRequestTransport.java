package com.rpc.remoting.transport;

import com.rpc.annotation.SPI;
import com.rpc.remoting.dto.RpcRequest;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcRequestTransport.java
 * @Description: send rpc request
 * @CreateTime: 2022/8/2 16:36:00
 **/
@SPI
public interface RpcRequestTransport {

    /**
     * send rpc request to server and get result
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
