package com.loeyae.springboot.demo.common;

import com.loeyae.springboot.demo.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * MD5Util .
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public class MD5Util {
    protected static final String STR_BYS_SECRET = "bys_secret";
    protected static final String STR_BYS_SIGNATURE = "bys_signature";

    private MD5Util() {
        throw new IllegalStateException("Utility class");
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new GlobalException("UTF-8 is unsupported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new GlobalException("MessageDigest不支持MD5Util", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) { hex.append("0"); };
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }


    /**
     * md5签名
     *
     * 按参数名称升序，将参数值进行连接 签名
     * @param appSecret
     * @param params
     * @return
     */
    public static String sign(String appSecret, TreeMap<String, String> params) {
        StringBuilder paramValues = new StringBuilder();
        params.put(STR_BYS_SECRET, appSecret);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramValues.append(entry.getValue());
        }
        return md5(paramValues.toString());
    }


    /**
     * 请求参数签名验证
     *
     * @param appSecret
     * @param request
     * @param signParams
     * @return true 验证通过 false 验证失败
     * @throws Exception
     */
    public static boolean verifySign(String appSecret, HttpServletRequest request,
                                     List<String> signParams) throws UnsupportedEncodingException {
        TreeMap<String, String> params = new TreeMap<String, String>();

        String signStr = request.getHeader(STR_BYS_SIGNATURE);
        if(StringUtils.isBlank(signStr)){
            throw new GlobalException("There is no signature field in the request parameter!");
        }

        for (String paramName:signParams) {
            params.put(paramName, URLDecoder.decode(request.getHeader(paramName), "UTF-8"));
        }

        if (!sign(appSecret, params).equals(signStr)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("appId", "1111");
        params.put("ts","134511551");
        sign("aaaa", params);
    }
}