package com.loeyae.springboot.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * category service.
 *
 * @date: 2019-10-30
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@FeignClient(url = "http://localhost:8804", name = "category")
public interface CategoryClient {

    @GetMapping("/category/node/{node_id}")
    Object getCategoryNode (@PathVariable("node_id") Integer nodeId);

}