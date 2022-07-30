package com.lemonzuo.jwt.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.lemonzuo.jwt.annotation.TokenIgnore;
import com.lemonzuo.jwt.constant.TokenConstant;
import com.lemonzuo.jwt.dto.UserLoginDTO;
import com.lemonzuo.jwt.util.UserTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author LemonZuo
 * @create 2022-07-29 22:25
 */
@RestController
@Slf4j
public class JwtController {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @TokenIgnore
    @RequestMapping("/generated")
    public UserLoginDTO generated() throws Exception {
        String loginId = IdUtil.fastUUID();
        String cacheKey = UserTokenUtils.getCacheKey(loginId);



        String token = UserTokenUtils.createToken(loginId);
        DateTime expire = DateUtil.offset(new Date(), DateField.MINUTE, TokenConstant.TOKEN_CACHE_TIME.intValue());
        UserLoginDTO dto = new UserLoginDTO();
        dto.setToken(token);
        dto.setExpire(expire);
        UserTokenUtils.cacheToken(cacheKey, dto);

        return dto;
    }

    @RequestMapping("/verify")
    public String verify() {
        return "verify";
    }

    @TokenIgnore
    @RequestMapping("/ignore")
    public String ignore() {
        return "ignore";
    }

    @RequestMapping("/ignore2")
    public String ignore2() {
        return "ignore2";
    }
}
