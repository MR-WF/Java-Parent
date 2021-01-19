package com.wf.netty.bean;

import java.io.Serializable;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-18 16:47
 **/
public class RpcResponse<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public static  <T> RpcResponse<T>  success(T data){
        RpcResponse<T> response = new RpcResponse();
        response.setCode(ResponseCodeEnum.SUCCESS.getCode());
        response.setMsg(ResponseCodeEnum.SUCCESS.getMessage());
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    /**
     * 失败响应
     * @param responseCodeEnum 响应码枚举
     * @param errorMessage 错误消息
     * @param <T> 泛型
     * @return RpcResponse
     */
    public static <T> RpcResponse<T> fail(ResponseCodeEnum responseCodeEnum, String errorMessage) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(responseCodeEnum.getCode());
        response.setMsg(errorMessage);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCodeEnum responseCodeEnum) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(responseCodeEnum.getCode());
        response.setMsg(responseCodeEnum.getMessage());
        return response;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
