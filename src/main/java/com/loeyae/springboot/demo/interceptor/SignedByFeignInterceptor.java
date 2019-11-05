package com.loeyae.springboot.demo.interceptor;

import java.io.*;

import com.loeyae.springboot.demo.common.AESUtil;
import com.loeyae.springboot.demo.common.ApiResult;
import com.loeyae.springboot.demo.common.MD5Util;
import com.loeyae.springboot.demo.exception.ApiResultException;
import com.loeyae.springboot.demo.service.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * 通过feign验证签名.
 *
 * @date: 2019-10-26
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
@Component
@Slf4j
public class SignedByFeignInterceptor extends SignedBaseInterceptor implements HandlerInterceptor {

    public static final String VERIFY_FAIL_MSG = "The request parameter signature verification failed!";
    private static final long TIMEOUT = 15000L;
    private List<String> signParams = new ArrayList<String>(Arrays.asList(str_bys_appId, str_bys_timestamp));

    @Autowired
    private ApiClient apiClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.warn("signed by feign");
        // appid
        String appId = request.getHeader(str_bys_appId);
        // 请求时时间戳
        String ts = request.getHeader(str_bys_timestamp);
        // 签名
        String signStr = request.getHeader(str_bys_signature);

        if (StringUtils.isBlank(appId) || StringUtils.isBlank(ts) || StringUtils.isBlank(signStr)) {
            log.warn(VERIFY_FAIL_MSG);
            ApiResult apiResult = new ApiResult();
            apiResult.setCode(HttpStatus.BAD_REQUEST.value());
            apiResult.setMsg("bad request");
            throw new ApiResultException(apiResult);
        }

        long currentTimeMillis = System.currentTimeMillis();
        long cTime = currentTimeMillis - Long.parseLong(ts);
        if (cTime > TIMEOUT) {
            log.warn(VERIFY_FAIL_MSG);
            ApiResult apiResult = new ApiResult();
            apiResult.setCode(HttpStatus.BAD_REQUEST.value());
            apiResult.setMsg("bad request");
            throw new ApiResultException(apiResult);
        }

        boolean byCache = false;
        boolean byFeign = false;
        String appSecret = null;
        while (!byCache || !byFeign) {
            if (Boolean.FALSE.equals(byCache)) {
                appSecret = getSecretByCache(appId);
                byCache = true;
            } else {
                appSecret = getSecretByFeign(appId);
                byFeign = true;
            }
            if (!StringUtils.isBlank(appSecret)) {
                if (byFeign) {
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

    private String getSecretByFeign(String appId) {
        ApiResult appSecretResult = apiClient.secret(appId);
        String appSecret = null;
        if (Boolean.TRUE.equals(appSecretResult.isOk())) {
            Map<String, String> appSecretData = (Map<String, String>) appSecretResult.getFeignData();
            appSecret = appSecretData.get("appSecret");
        }
        if (StringUtils.isBlank(appSecret)) {
            log.warn(VERIFY_FAIL_MSG);
            ApiResult apiResult = new ApiResult();
            apiResult.setCode(HttpStatus.FORBIDDEN.value());
            apiResult.setMsg("forbidden");
            throw new ApiResultException(apiResult);
        }
        return appSecret;
    }

    public static void main(String[] args) {
        SignedByFeignInterceptor signedByFeignInterceptor = new SignedByFeignInterceptor();
        signedByFeignInterceptor.cacheSecret("111111", "2222222");
        String secret = signedByFeignInterceptor.getSecretByCache("111111");
        System.out.println(secret);
    }
}