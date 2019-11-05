package com.loeyae.springboot.demo.exception;

import com.loeyae.springboot.demo.common.ApiResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FeignException.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@AllArgsConstructor
@Data
public class FeignException extends RuntimeException {
    private static final long serialVersionUID = -7146685561602901532L;


    /**
     * 错误码
     */
    private ApiResult apiResult;
}