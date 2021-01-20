package com.wf.netty.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-19 14:57
 **/
@Data
public class RpcRequset implements Serializable {

    //服务名
    private String serviceName;
    //方法名
    private String methodName;
    //版本号
    private String version;
    //参数类型数据
    private Class<?> [] type;
    //参数
    private Object[] params;

}
