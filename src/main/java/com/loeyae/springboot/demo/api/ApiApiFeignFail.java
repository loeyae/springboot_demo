package com.loeyae.springboot.demo.api;

import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.entity.Demo;

/**
 * FeignFail.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public class ApiApiFeignFail implements ApiApi {

    @Override
    public ApiResult index(Integer id) {
        return ApiResult.feignFail();
    }

    @Override
    public ApiResult post(Demo demo) {
        return ApiResult.feignFail();
    }

    @Override
    public ApiResult open(Integer id) {
        return ApiResult.feignFail();
    }

    @Override
    public ApiResult secret(String appId) {
        return ApiResult.feignFail();
    }
}