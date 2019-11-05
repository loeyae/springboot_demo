package com.loeyae.springboot.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.service.ApiClient;
import com.loeyae.springboot.demo.service.ApplicationClient;
import com.loeyae.springboot.demo.service.CategoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Feign调用.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Controller
public class FeignController {

    @Value("${app.id}")
    private String appId;

    @Autowired
    ApiClient apiClient;

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    ApplicationClient applicationClient;

    @ResponseBody
    @RequestMapping(value = "/feign/index", method = RequestMethod.GET)
    public Object index() {
        return apiClient.index(10).getFeignData();
    }

    @ResponseBody
    @RequestMapping(value = "/feign/secret", method = RequestMethod.GET)
    public Object secret() {
        return apiClient.secret(appId).getFeignData();
    }

    @ResponseBody
    @RequestMapping(value = "/feign/open", method = RequestMethod.GET)
    public Object open() {
        return apiClient.open(null).getFeignData();
    }

    @ResponseBody
    @RequestMapping(value = "/feign/category", method = RequestMethod.GET)
    public Object category() {
        return categoryClient.getCategoryNode(1);
    }

    @ResponseBody
    @RequestMapping(value = "/feign/application")
    public Object application() {
        return applicationClient.getApplicationByAppId(appId);
    }
}