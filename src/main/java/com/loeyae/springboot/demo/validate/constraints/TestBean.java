/*
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * see LICENSE for more details: http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.loeyae.springboot.demo.validate.constraints;

import com.loeyae.springboot.demo.validate.validator.TestBeanValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *
 * @author Zhang Yi <loeyae@gmail.com>
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {TestBeanValidator.class})
public @interface TestBean {

    /**
     * 是否校验
     *
     * @return
     */
    boolean required() default true;

    /**
     * 默认错误消息
     *
     * @return
     */
    String message() default "category node not exists";

    /**
     * 验证约束组
     *
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * 约束注解的有效负载
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @IsValidNode} constraints on the same element.
     *
     * @see TestBean
     */
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        TestBean[] value();
    }
}
