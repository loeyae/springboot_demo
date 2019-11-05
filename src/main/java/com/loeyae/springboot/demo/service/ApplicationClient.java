package com.loeyae.springboot.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * application client.
 *
 * @date: 2019-10-30
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@FeignClient(url = "http://localhost:8801", name = "application")
public interface ApplicationClient {

    @GetMapping("/v1.0/application/get/secret-by-id")
    Object getApplicationByAppId(@RequestParam("id") String id);

}