package com.loeyae.springboot.demo.common;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密工具类.
 *
 * @date: 2019-10-28
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public class AESUtil {

    /**
     * 加密
     *
     * @param seed
     * @param source
     * @return
     */
    public static String encrypt(String seed, String source) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            Key key = getKey(seed);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(source.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param seed
     * @param secret
     * @return
     */
    public static String decrypt(String seed, String secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            Key key = getKey(seed);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(secret));
            return new String(result,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取加密的key
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static Key getKey(String seed) throws NoSuchAlgorithmException {
        // 生成KEY
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom(seed.getBytes());
        keyGenerator.init(128, secureRandom);
        // 产生密钥
        SecretKey secretKey = keyGenerator.generateKey();
        // 获取密钥
        byte[] keyBytes = secretKey.getEncoded();
        Key key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }
}