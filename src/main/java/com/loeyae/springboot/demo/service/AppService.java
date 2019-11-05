package com.loeyae.springboot.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * app service.
 *
 * @date: 2019-10-25
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Service
public class AppService {

    @Value("${app.id}")
    private String appId;

    @Value("${app.secret}")
    private String appSecret;

    public String getSecret(String appId) {
        if (appId.equals(this.appId)) {
            return appSecret;
        }
        return "";
    }
}