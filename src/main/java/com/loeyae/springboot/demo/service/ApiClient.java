package com.loeyae.springboot.demo.service;

import com.loeyae.springboot.demo.api.ApiApi;
import org.springframework.cloud.openfeign.FeignClient;

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