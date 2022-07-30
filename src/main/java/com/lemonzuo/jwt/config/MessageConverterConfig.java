package com.lemonzuo.jwt.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LemonZuo
 * @create 2022-07-30 16:42
 */
// @Configuration
public class MessageConverterConfig extends WebMvcConfigurationSupport {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建会话消息实例容器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //创建fastJson配置实例
        FastJsonConfig config = new FastJsonConfig();
        config.setWriterFeatures(
                // 保留 Map 空的字段
                JSONWriter.Feature.WriteMapNullValue,
                // 将 String 类型的 null 转成""
                JSONWriter.Feature.WriteNullStringAsEmpty,
                // 将 Number 类型的 null 转成 0
                // JSONWriter.Feature.WriteNullNumberAsZero,
                // 将 List 类型的 null 转成 []
                JSONWriter.Feature.WriteNullListAsEmpty,
                // 将 Boolean 类型的 null 转成 false
                JSONWriter.Feature.WriteNullBooleanAsFalse,
                //返回Json数据排版格式
                JSONWriter.Feature.PrettyFormat);
        //按字段名称排序后输出-SerializerFeature.SortField
        //设置配置实例
        converter.setFastJsonConfig(config);
        //设置默认编码方式
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        //集合填入媒介类型
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在 Controller 上的 @RequestMapping 中加了个属性 produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        // mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        //设置支持媒介——装载了解决中文乱码参数
        converter.setSupportedMediaTypes(mediaTypeList);
        //添加到会话中
        converters.add(converter);
    }
}
