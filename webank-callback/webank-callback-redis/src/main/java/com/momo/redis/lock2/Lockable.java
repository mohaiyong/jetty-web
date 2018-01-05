package com.momo.redis.lock2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解.
 * Created by Administrator on 2017/4/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lockable {

    String[] key() default "";

    /**
     * 等待时间, 单位秒.
     * @return
     */
    long maximumWaiteTime() default 0;

    /**
     * 锁持续时间，单位秒.
     */
    long expirationTime() default 1000;
}
