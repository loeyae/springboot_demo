package com.loeyae.springboot.demo.common;

/**
 * error code.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public interface IErrorCode {
    /**
     * 错误编码 -1、失败 0、成功
     */
    long getCode();

    /**
     * 错误描述
     */
    String getMsg();

    /**
     * 数据
     */
    Object getData();
}