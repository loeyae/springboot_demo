package com.loeyae.springboot.demo.service;

import com.loeyae.springboot.demo.api.ApiApi;
import com.loeyae.springboot.demo.api.ApiApiFeignFail;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * api feign client.
 *
 * @date: 2019-10-25
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Component
@FeignClient(url = "${service.demo.url}", name = "${service.demo.name}", fallbackFactory = ApiFeignFallbackFactory.class)
public interface ApiClient extends ApiApi {

//    @RequestMapping(method = RequestMethod.GET, value = "api/open/index", consumes = "application" +
//            "/json;charset=UTF-8")
//    String index(@RequestParam(value = "id") Integer id);
}

@Slf4j
@Configuration
class ApiFeignFallbackFactory implements FallbackFactory<ApiApiFeignFail> {

    @Override
    public ApiApiFeignFail create(Throwable throwable) {
        return new ApiFeignFail();
    }
}

class ApiFeignFail extends ApiApiFeignFail {

}