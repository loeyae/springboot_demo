package com.loeyae.springboot.demo.interceptor;

import com.loeyae.springboot.demo.common.AESUtil;

import java.io.*;
import java.util.Base64;

/**
 * 基类.
 *
 * @date: 2019-10-28
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public class SignedBaseInterceptor {


    protected static final String str_bys_appId = "bys_appId";
    protected static final String str_bys_signature = "bys_signature";
    protected static final String str_bys_secret = "bys_secret";
    protected static final String str_bys_timestamp = "bys_timestamp";

    protected String getSecretByCache(String appId) {
        String appSecret = null;
        File file = getCacheFile(appId);
        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getPath()));
                appSecret = bufferedReader.readLine();
                return AESUtil.decrypt(appId, appSecret);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appSecret;
    }

    protected void cacheSecret(String appId, String appSecret) {
        try {
            File file = getCacheFile(appId);
            String encryptString = AESUtil.encrypt(appId, appSecret);
            if (Boolean.FALSE.equals(file.exists())) {
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getPath()));
            bufferedWriter.write(encryptString);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getCacheFile(String appId) {
        String tmpdir = System.getProperty("java.io.tmpdir");
        String fileName = Base64.getEncoder().encodeToString(appId.getBytes());
        return new File(tmpdir, fileName);
    }
}