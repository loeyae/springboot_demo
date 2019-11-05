/*
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * see LICENSE for more details: http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.loeyae.springboot.demo.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loeyae.springboot.demo.common.QueryWapperUtils;
import com.loeyae.springboot.demo.common.ValidateUtil;
import com.loeyae.springboot.demo.entity.Demo;
import com.loeyae.springboot.demo.entity.JsonDemoVo;
import com.loeyae.springboot.demo.entity.TestDemoBo;
import com.loeyae.springboot.demo.validate.groups.SampleValidation;
import com.loeyae.springboot.demo.validate.groups.TestValidation;

import java.beans.PropertyDescriptor;
import java.io.File;
import javax.validation.constraints.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author Zhang Yi <loeyae@gmail.com>
 */
@Controller
@Slf4j
public class IndexController {

    int a = 1;

    @ResponseBody
    @RequestMapping("hello")
    public String hello() {
        return "Hellow World";
    }

    @RequestMapping("index.htm")
    public String index(ModelMap map) {
        map.addAttribute("hello", "Hellow Thymeleaf!");
        log.error("This is test logging");
        return "index";
    }

    @RequestMapping("valid")
    public String valid(@RequestParam("id") Integer id) throws Exception {
        TestDemoBo testDemoBo = new TestDemoBo();
//        testDemoBo.setId(id);
//        ValidateUtil.validateEntity(testDemoBo);
        ValidateUtil.validateParamter(TestDemoBo.class, "id", id, TestValidation.class, SampleValidation.class);
        return "index";
    }

    @RequestMapping("form.htm")
    public String form(ModelMap map) {
        map.addAttribute("title", "My form");
        return "form";
    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public JsonDemoVo upload(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        Map<String, String[]> parameter = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameter.entrySet()) {
            String key = entry.getKey();
            String[] val = entry.getValue();
            log.error(key);
            for (String v : val) {
                log.error(v);
            }
        }
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);
            /**
             * 获取文件的后缀*
             */
            String filename = multipartFile.getOriginalFilename();

            String path = request.getServletContext().getRealPath("/upload/");
            log.error(path);

            // 获取上传的文件名称，并结合存放路径，构建新的文件名称
            File filepath = new File(path, filename);

            // 判断路径是否存在，不存在则新创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }

            // 将上传文件保存到目标文件目录
            multipartFile.transferTo(new File(path + File.separator + filename));

        }
        JsonDemoVo jdv = new JsonDemoVo();
        return jdv;
    }

    public static void main(String[] args) {
        TestDemoBo testDemoBo = new TestDemoBo();
        testDemoBo.setId(1);
        testDemoBo.setName("test");
        testDemoBo.setNameLk("txt");
        testDemoBo.setStatusStr("INIT");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date = sdf.parse("2019.09.23");
            testDemoBo.setCreateTimeStart(date);
        } catch (Throwable e) {
            ;
        }
        Class<?> targetClass = Demo.class;
        QueryWrapper queryWrapper = QueryWapperUtils.queryToWrapper(testDemoBo, targetClass);
        System.out.println(queryWrapper.getSqlSegment());
    }
}
