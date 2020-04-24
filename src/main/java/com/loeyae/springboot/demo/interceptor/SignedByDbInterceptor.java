package com.loeyae.springboot.demo.interceptor;

import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.common.MD5Util;
import com.loeyae.springboot.demo.exception.ApiResultException;
import com.loeyae.springboot.demo.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 签名拦截器.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Component
@Slf4j
public class SignedByDbInterceptor extends SignedBaseInterceptor implements HandlerInterceptor {

    public static final String VERIFY_FAIL_MSG = "The request parameter signature verification failed!";
    private List<String> signParams = new ArrayList<>(Arrays.asList(STR_BYS_APP_ID, STR_BYS_TIMESTAMP));

    @Autowired
    private AppService appService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        log.warn("signed by db");
        // appid
        String appId = request.getHeader(STR_BYS_APP_ID);
        // 请求时时间戳
        String ts = request.getHeader(STR_BYS_TIMESTAMP);
        // 签名
        String signStr = request.getHeader(STR_BYS_SIGNATURE);

        if (StringUtils.isBlank(appId) || StringUtils.isBlank(ts) || StringUtils.isBlank(signStr)) {
            log.warn(VERIFY_FAIL_MSG);
            ApiResult apiResult = new ApiResult();
            apiResult.setCode(HttpStatus.BAD_REQUEST.value());
            apiResult.setMsg("bad request");
            throw new ApiResultException(apiResult);
        }

        boolean byCache = false;
        boolean byDb = false;
        String appSecret = null;
        while (!byCache || !byDb) {
            if (Boolean.FALSE.equals(byCache)) {
                appSecret = getSecretByCache(appId);
                byCache = true;
            } else {
                appSecret = getSecretByDB(appId);
                byDb = true;
            }
            if (!StringUtils.isBlank(appSecret)) {
                if (byDb) {
                    cacheSecret(appId, appSecret);
                }
                boolean right = MD5Util.verifySign(appSecret, request, signParams);
                if (right) {
                    return true;
                }
            }
        }

        log.warn(VERIFY_FAIL_MSG);
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(HttpStatus.UNAUTHORIZED.value());
        apiResult.setMsg("unauthorized");
        throw new ApiResultException(apiResult);
    }

    private String getSecretByDB(String appId) {

        String appSecret = appService.getSecret(appId);
        if (StringUtils.isBlank(appSecret)) {
            log.warn(VERIFY_FAIL_MSG);
            ApiResult apiResult = new ApiResult();
            apiResult.setCode(HttpStatus.FORBIDDEN.value());
            apiResult.setMsg("forbidden");
            throw new ApiResultException(apiResult);
        }
        return appSecret;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        return ;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        return ;
    }
}