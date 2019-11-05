package com.loeyae.springboot.demo.api;

import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.entity.Demo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * api.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public interface ApiApi {

    @GetMapping(value = "/api/index")
    ApiResult index(@RequestParam(name = "id", required = false) Integer id);

    @PostMapping(value = "/api/post")
    ApiResult post(@RequestBody Demo demo);

    @GetMapping(value = "/api/open/index")
    ApiResult open(@RequestParam(name = "id", required = false) Integer id);

    @GetMapping(value = "/api/app/secret")
    ApiResult secret(@RequestParam(name = "appId") String appId);
}