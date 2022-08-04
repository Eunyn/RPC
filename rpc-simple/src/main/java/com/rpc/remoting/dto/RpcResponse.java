package com.rpc.remoting.dto;

import java.io.Serializable;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcResponse.java
 * @Description: 封装的响应对象
 * @CreateTime: 2022/7/24 17:37:00
 **/
public class RpcResponse implements Serializable {

    /**
     * 请求的对象 ID
     */
    private String requestId;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 返回的结果
     */
    private Object result;

    public RpcResponse(){}

    public RpcResponse(String requestId, String error, Object result) {
        this.requestId = requestId;
        this.error = error;
        this.result = result;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
