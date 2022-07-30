package com.lemonzuo.jwt.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lemonzuo.jwt.annotation.TokenIgnore;
import com.lemonzuo.jwt.constant.TokenConstant;
import com.lemonzuo.jwt.util.UserTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author LemonZuo
 * @create 2022-07-29 23:13
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            TokenIgnore tokenIgnore = handlerMethod.getMethodAnnotation(TokenIgnore.class);
            if (tokenIgnore != null) {
                return true;
            }
        }
        String token = request.getHeader(TokenConstant.HEAD_TOKEN_KEY);
        if (StrUtil.isEmptyIfStr(token)) {
            writeErrorInfo(response, "未登录");
            return false;
        }

        Claims claims = UserTokenUtils.parseToken(token);
        String loginId = claims.get(TokenConstant.CLAIMS_KEY, String.class);
        String cacheKey = UserTokenUtils.getCacheKey(loginId);
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        if (Boolean.TRUE.equals(hasKey)) {
            UserTokenUtils.refreshToken(loginId);
            return true;
        } else {
            writeErrorInfo(response, "token令牌失效请重新登录");
            return false;
        }
    }

    private void writeErrorInfo(HttpServletResponse response, String message) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        JSONObject error = new JSONObject();
        error.put("code", -1);
        error.put("message", message);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(error.toJSONString());
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
