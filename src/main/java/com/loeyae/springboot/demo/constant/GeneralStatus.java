package com.loeyae.springboot.demo.constant;

/**
 * 状态.
 *
 * @date: 2019-09-23
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public enum GeneralStatus {
    INIT(0, "INIT"),
    ACTIVE(1, "ACTIVE"),
    ;

    private final int code;
    private final String message;

    GeneralStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
