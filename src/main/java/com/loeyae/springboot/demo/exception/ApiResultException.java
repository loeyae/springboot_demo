package com.loeyae.springboot.demo.exception;

import com.loeyae.springboot.demo.common.ApiResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ApiResultException.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@AllArgsConstructor
@Data
public class ApiResultException extends RuntimeException {
    private static final long serialVersionUID = -899285216110088451L;

    /**
     * 错误码
     */
    private ApiResult apiResult;

}