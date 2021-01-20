package com.wf.netty.vo;

public interface IResponseCodeEnum {
    /**
     * 获取状态码
     * @return 状态码
     */
    public Integer getCode();
    /**
     * 获取提示信息
     * @return 提示信息
     */
    public String getMessage();

}
