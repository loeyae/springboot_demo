/*
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * see LICENSE for more details: http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.loeyae.springboot.demo.exception;

import com.loeyae.springboot.demo.common.BaseResponse;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
/**
 *
 * @author Zhang Yi <loeyae@gmail.com>
 */
@Component
public class BindExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex.getClass().equals(ConstraintViolationException.class)) {
            ConstraintViolationException exception = (ConstraintViolationException) ex;

            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<?> violation : violations ) {
                stringBuilder.append(violation.getMessage());
            }
            HashMap<String, String> errorMap = new HashMap<>();
            errorMap.put("message", stringBuilder.toString());
            return new ModelAndView(new MappingJackson2JsonView(), errorMap);
        }
        return null;
    }
}
//@ControllerAdvice("com.loeyae.springboot.demo.controller")
//@Slf4j
//public class BindExceptionResolver {
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public BaseResponse businessExceptionHandler(HttpServletResponse response, ConstraintViolationException ex) {
//        response.setStatus(HttpStatus.METHOD_FAILURE.value());
//        log.info(ex.getMessage(), ex);
//        return new BaseResponse(500, ex.getMessage());
//    }
//
//}
