package com.loeyae.springboot.demo.config;

import com.loeyae.springboot.demo.interceptor.SignedByDbInterceptor;
import com.loeyae.springboot.demo.interceptor.SignedByFeignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * 配置拦截器.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
//@Configuration
public class IntercepterConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 签名校验
        List<String> excludPath = Arrays.asList("/api/open/**", "/api/app/**");
        registry.addInterceptor(getSignedByFeignInterceptor()).addPathPatterns("/api/**").excludePathPatterns(excludPath);
        registry.addInterceptor(getSignedByDbInterceptor()).addPathPatterns("/api/app/**");
    }

    @Bean
    public HandlerInterceptor getSignedByDbInterceptor() {
        return new SignedByDbInterceptor();
    }

    @Bean
    public HandlerInterceptor getSignedByFeignInterceptor() {
        return new SignedByFeignInterceptor();
    }
}