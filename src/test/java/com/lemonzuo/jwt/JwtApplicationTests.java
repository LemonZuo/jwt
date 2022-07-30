package com.lemonzuo.jwt;

import cn.hutool.core.util.IdUtil;
import com.lemonzuo.jwt.constant.JwtConstant;
import com.lemonzuo.jwt.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
// @SpringBootTest
class JwtApplicationTests {
    @Test
    public void generatedRsa() {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        log.info("getPublic:{}", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        log.info("getPrivate:{}", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
    }


    @Test
    public void generated() throws Exception {
        String uuid = IdUtil.fastUUID();
        Map<String, Object> claims = new HashMap<>();
        claims.put("login_id", uuid);

        PrivateKey privateKey = JwtUtils.getPrivateKey(JwtConstant.PRIVATE_SECRET);
        // 私钥加密
        String token = Jwts.builder().setClaims(claims).signWith(privateKey, SignatureAlgorithm.RS256).compact();
        log.info("token:{}", token);

        // 公钥解密
        PublicKey publicKey = JwtUtils.getPublicKey(JwtConstant.PUBLIC_SECRET);
        Claims claim = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token).getBody();
        String loginId = claim.get("login_id", String.class);
        log.info("loginId:{}", loginId);
    }

}
