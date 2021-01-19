package com.wf.netty.bean;

public enum ResponseCodeEnum implements IResponseCodeEnum{

    SUCCESS(0,"success"),FILE(-1,"file");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
