package com.lemonzuo.jwt.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author LemonZuo
 * @create 2022-07-30 13:58
 */
@Slf4j
public class TokenConstant {
    /**
     * token 缓存时间
     */
    public static final Long TOKEN_CACHE_TIME = 30L;
    /**
     * token 刷新时间
     */
    public static final Long TOKEN_DIFF_TIME = 10L;
    /**
     * 请求头token
     */
    public static final String HEAD_TOKEN_KEY = "auth-token";
    /**
     * claims key值
     */
    public static final String CLAIMS_KEY = "login_id";
}
