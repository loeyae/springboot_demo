/*
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * see LICENSE for more details: http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.loeyae.springboot.demo.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Zhang Yi <loeyae@gmail.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    
    /**
     * 应答状态码.
     */
    private int status = 200;

    /**
     * 应答消息.
     */
    private String message = "SUCCESS";
}
