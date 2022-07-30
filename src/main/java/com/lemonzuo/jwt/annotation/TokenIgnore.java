package com.lemonzuo.jwt.annotation;

import java.lang.annotation.*;

/**
 * @author LemonZuo
 * @create 2022-07-29 23:23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenIgnore {
}
