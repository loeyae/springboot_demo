package com.loeyae.springboot.demo.interceptor;

import com.loeyae.springboot.demo.common.MD5Util;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * feign拦截器.
 *
 * @date: 2019-10-25
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Configuration
@Slf4j
public class FeignInterceptor implements RequestInterceptor {

    @Value("${app.id}")
    private String appId;

    @Value("${app.secret}")
    private String appSecret;

    private static final String str_bys_appId = "bys_appId";
    private static final String str_bys_signature = "bys_signature";
    private static final String str_bys_secret = "bys_secret";
    private static final String str_bys_timestamp = "bys_timestamp";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //过滤open api
        if (requestTemplate.url().matches("/api/open/(.*)")) {
            return;
        }

        log.warn("请求参数为：{}", requestTemplate.queryLine());
        Long ts = System.currentTimeMillis();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("appId", appId);
        params.put("ts", ts.toString());
        String sign = MD5Util.sign(appSecret, params);
        requestTemplate.header(str_bys_appId, appId);
        requestTemplate.header(str_bys_timestamp, ts.toString());
        requestTemplate.header(str_bys_signature, sign);
        log.warn("header参数为：{}", requestTemplate.headers());
    }

}