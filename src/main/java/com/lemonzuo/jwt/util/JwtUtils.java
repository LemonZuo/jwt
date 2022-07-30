package com.lemonzuo.jwt.util;

import com.lemonzuo.jwt.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LemonZuo
 * @create 2022-07-30 15:01
 */
@Slf4j
public class JwtUtils {
    /**
     * rsa 公钥
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * rsa 私钥
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 生成jwt token
     * @param loginId
     * @return
     * @throws Exception
     */
    public static String createToken(String loginId) throws Exception {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put("login_id", loginId);
        PrivateKey privateKey = getPrivateKey(JwtConstant.PRIVATE_SECRET);
        String token = Jwts.builder().setClaims(claims).signWith(privateKey, SignatureAlgorithm.RS256).compact();
        log.info("token:{}", token);
        return token;
    }

    /**
     * 解析JWT token
     * @param token
     * @return
     * @throws Exception
     */
    public static Claims parseToken(String token) throws Exception {
        PublicKey publicKey = getPublicKey(JwtConstant.PUBLIC_SECRET);
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();
    }
}
