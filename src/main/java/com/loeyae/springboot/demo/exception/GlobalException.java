package com.loeyae.springboot.demo.exception;

import com.loeyae.springboot.demo.common.IErrorCode;

/**
 * GlobalException.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 2543302736830708789L;
    /**
     * 错误码
     */
    private IErrorCode errorCode;

    public GlobalException(IErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}