package com.financial.common.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import com.financial.common.core.constant.SecurityConstants;
import com.financial.common.core.constant.TokenConstants;
import com.financial.common.core.text.Convert;
import javax.crypto.SecretKey;

/**
 * Jwt工具类
 *
 * @author xinyi
 */
public class JwtUtils {
    public static String secret = TokenConstants.SECRET;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims) {
        return JWT.create()
            .withPayload(claims)
            .withExpiresAt(new Date(System.currentTimeMillis()+3600_000))
            .sign(getAlgorithm(secret));
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static DecodedJWT parseToken(String token) {
        return JWT.require(getAlgorithm(secret))
            .build()
            .verify(token);
    }
    /**
     * 根据令牌获取用户标识
     * 
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token) {
        DecodedJWT jwt= parseToken(token);
        return jwt.getClaim(SecurityConstants.USER_KEY).toString();
    }

    /**
     * 根据令牌获取用户标识
     * 
     * @param jwt 身份信息
     * @return 用户ID
     */
    public static String getUserKey(DecodedJWT jwt) {
        return getValue(jwt, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     * 
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token) {
        DecodedJWT jwt = parseToken(token);
        return getValue(jwt, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     * 
     * @param jwt 身份信息
     * @return 用户ID
     */
    public static String getUserId(DecodedJWT jwt) {
        return getValue(jwt, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     * 
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserName(String token) {
        DecodedJWT jwt = parseToken(token);
        return getValue(jwt, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     * 
     * @param jwt 身份信息
     * @return 用户名
     */
    public static String getUserName(DecodedJWT jwt) {
        return getValue(jwt, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取键值
     */
    public static String getValue(DecodedJWT jwt, String key) {
        Claim claim = jwt.getClaim(key);
        return claim.isNull() ? "" : Convert.toStr(claim.asString());
    }

    /**
     * 获取加密算法
     * @param secret 密钥
     * @return 算法
     */
    private static Algorithm getAlgorithm(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        int bitLength = keyBytes.length * 8;

        if (bitLength >= 512) {
            return Algorithm.HMAC512(secret);
        } else if (bitLength >= 384) {
            return Algorithm.HMAC384(secret);
        } else {
            return Algorithm.HMAC256(secret);
        }
    }
}
