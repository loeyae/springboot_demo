package com.loeyae.springboot.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.loeyae.springboot.demo.api.ApiApi;
import com.loeyae.springboot.demo.common.ApiResult;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * api feign client.
 *
 * @date: 2019-10-25
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@FeignClient(url = "http://localhost:8081", name = "api")
public interface ApiClient extends ApiApi {

//    @RequestMapping(method = RequestMethod.GET, value = "api/open/index", consumes = "application" +
//            "/json;charset=UTF-8")
//    String index(@RequestParam(value = "id") Integer id);
}