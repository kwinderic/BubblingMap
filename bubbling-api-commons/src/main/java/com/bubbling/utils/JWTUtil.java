package com.bubbling.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * 2022-03-04 23:45:24 GMT+8
 * JWTUtil工具类
 * @author k
 */
public class JWTUtil {

    /**
     * 2022-03-04 23:40:30 GMT+8
     * 设置密钥
     * @param secretKey 自定义密钥
     * @author k
     */
    public static void setSecretKey(String secretKey){
        ConstantUtil.JWTSecretKey =secretKey;
    }

    /**
     * 2022-03-04 23:40:49 GMT+8
     * 获取jwt-token
     * @param map payload键值对
     * @author k
     */
    public static String getJWTToken(Map<String,String> map){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, 168);   //7 days
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        return builder.withExpiresAt(instance.getTime()). //指定令牌过期时间
                sign(Algorithm.HMAC256(ConstantUtil.JWTSecretKey));
    }

    /**
     * 2022-03-04 23:41:05 GMT+8
     * 验证token合法性并返回获取token信息的类
     * @param token jwt-token
     * @author k
     */
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(ConstantUtil.JWTSecretKey)).build().verify(token);
    }
}
