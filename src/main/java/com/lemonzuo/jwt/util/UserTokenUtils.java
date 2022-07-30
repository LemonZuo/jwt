package com.lemonzuo.jwt.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.lemonzuo.jwt.constant.TokenConstant;
import com.lemonzuo.jwt.dto.UserLoginDTO;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author LemonZuo
 * @create 2022-07-30 15:28
 */
public class UserTokenUtils {
    private static final RedisTemplate<String, UserLoginDTO> redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
    private static final String CACHE_PREFIX = "login_id:";
    public static String getCacheKey(String loginId) {
        return CACHE_PREFIX.concat(loginId);
    }

    public static String createToken(String loginId) throws Exception {
        if (StrUtil.isEmptyIfStr(loginId)) {
            throw new RuntimeException("loginId 不能为空");
        }
        return JwtUtils.createToken(loginId);
    }

    public static void cacheToken(String cacheKey, UserLoginDTO cacheData) {
        redisTemplate.opsForValue().set(cacheKey, cacheData, TokenConstant.TOKEN_CACHE_TIME, TimeUnit.MINUTES);
    }

    public static Claims parseToken(String token) throws Exception {
        if (StrUtil.isEmptyIfStr(token)) {
            throw new RuntimeException("loginId 不能为空");
        }
        return JwtUtils.parseToken(token);
    }

    public static void refreshToken(String loginId) {
        if (StrUtil.isEmptyIfStr(loginId)) {
            throw new RuntimeException("loginId 不能为空");
        }
        String cacheKey = getCacheKey(loginId);
        UserLoginDTO cacheData = redisTemplate.opsForValue().get(cacheKey);
        assert cacheData != null;
        Date expire = cacheData.getExpire();
        long diff = DateUtil.between(expire, new Date(), DateUnit.MINUTE);
        if (diff < TokenConstant.TOKEN_DIFF_TIME) {
            expire = DateUtil.offset(expire, DateField.MINUTE, TokenConstant.TOKEN_CACHE_TIME.intValue());
            cacheData.setExpire(expire);
            cacheToken(cacheKey, cacheData);
        }
    }
}
