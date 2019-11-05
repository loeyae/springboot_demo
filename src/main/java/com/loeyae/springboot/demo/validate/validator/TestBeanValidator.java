/*
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * see LICENSE for more details: http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.loeyae.springboot.demo.validate.validator;

import com.loeyae.springboot.demo.entity.TestDemoBo;
import com.loeyae.springboot.demo.validate.constraints.TestBean;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Objects;

/**
 *
 * @author Zhang Yi <loeyae@gmail.com>
 */
public class TestBeanValidator implements ConstraintValidator<TestBean, TestDemoBo> {

     /**
     * 是否必须
     */
    private boolean required = false;

    /**
     * 初始化
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(TestBean constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    /**
     * 验证
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(TestDemoBo value, ConstraintValidatorContext context) {
        
        if (required == false && Objects.isNull(value)) {
            return true;
        }
        System.out.println(value.getId());
        return false;
    }

}
