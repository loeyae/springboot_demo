/*
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * see LICENSE for more details: http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.loeyae.springboot.demo.entity;

import lombok.Data;

/**
 *
 * @author Zhang Yi <loeyae@gmail.com>
 */
@Data
public class JsonDemoVo {

    private Integer code = 200;

    private String msg = "Ok";

    private Object obj;

}
