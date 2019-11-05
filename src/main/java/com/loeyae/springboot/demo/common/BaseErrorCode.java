package com.loeyae.springboot.demo.common;

/**
 * BaseErrorCode.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public enum BaseErrorCode implements IErrorCode {
    /**
     * 失败
     */
    FAILED(-1, "执行失败"),

    ERROR(-2, "缺少参数"),

    /**
     * 成功
     */
    SUCCESS(0, "执行成功"),

    /**
     *  请持有有效令牌
     */
    TOKEN_INVALID(401, ""),
    /**
     *  token超时
     */
    TOKEN_EXPIRE(409, ""),

    /**
     * feign服务调用失败
     */
    FEIGN_FAIL(509, "服务调用失败"),
    ;

    private final long code;
    private final String msg;

    BaseErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BaseErrorCode fromCode(long code) {
        BaseErrorCode[] ecs = BaseErrorCode.values();
        for (BaseErrorCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg);
    }
}
