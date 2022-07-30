package com.lemonzuo.jwt.config;

import com.lemonzuo.jwt.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author ZKQ
 * @date 2021-03-04 16:57
 * 自定义web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                // 拦截路径
                .addPathPatterns("/**")
                .excludePathPatterns("/ignore2");
    }
}
