package com.wf.netty.vo;

public enum ResponseCodeEnum implements IResponseCodeEnum{

    SUCCESS(0,"success"),FAIL(-1,"fail");


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
