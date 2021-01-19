package com.wf.netty.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 16:28
 **/
public class RpcRequset implements Serializable {

    private String className;
    private String methodName;
    private String version;
    private Class<?> [] type;
    private Object[] params;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getType() {
        return type;
    }

    public void setType(Class<?>[] type) {
        this.type = type;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "RpcRequset{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", version='" + version + '\'' +
                ", type=" + Arrays.toString(type) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
