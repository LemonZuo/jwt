package com.lemonzuo.jwt.dto;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author LemonZuo
 * @create 2022-07-30 17:52
 */
@Data
public class UserLoginDTO {
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    private Date expire;
    private String token;
}
