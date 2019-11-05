package com.loeyae.springboot.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loeyae.springboot.demo.api.ApiApi;
import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.entity.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * api controller.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@RestController
@Slf4j
public class ApiController implements ApiApi {

    @Value("${app.id}")
    private String appId;

    @Value("${app.secret}")
    private String appSecret;

    @Override
    public ApiResult index(@RequestParam(name = "id", required = false) Integer id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data","test");
        return ApiResult.ok(jsonObject);
    }

    @Override
    public ApiResult post(@RequestBody Demo demo) {
        return ApiResult.ok(demo);
    }

    @Override
    public ApiResult open(@RequestParam(name = "id", required = false) Integer id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data","open");
        return ApiResult.ok(jsonObject);
    }

    @Override
    public ApiResult secret(String appId) {
        if (appId.equals(this.appId)) {
            Map<String, String> param = new HashMap<>();
            param.put("appSecret", appSecret);
            return ApiResult.ok(param);
        }
        return ApiResult.failed("app no exists");
    }
}
