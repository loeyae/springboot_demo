/*
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * see LICENSE for more details: http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.loeyae.springboot.demo.entity;

import com.loeyae.springboot.demo.validate.constraints.TestBean;
import com.loeyae.springboot.demo.validate.groups.InsertValidation;
import com.loeyae.springboot.demo.validate.groups.SampleValidation;
import com.loeyae.springboot.demo.validate.groups.TestValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.*;
/**
 *
 * @author Zhang Yi <loeyae@gmail.com>
 */
@Data
@NoArgsConstructor
@TestBean(message = "test bean", groups = {TestValidation.class})
public class TestDemoBo implements Serializable{
    private static final long serialVersionUID = 1L;

    @Min(value = 10, message = "min 10", groups = {InsertValidation.class})
    @Min(value = 10, message = "min 10 test", groups = {TestValidation.class})
    @Min(value = 10, message = "min 10 sample", groups = {SampleValidation.class})
    private Integer id;

    private String name;

    private String nameLk;

    private String statusStr;

    private Date createTimeStart;

}
