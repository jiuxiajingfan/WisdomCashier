package com.li.wisdomcashier.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedissonLock {
    String key();

    String keyPrefix() default "";

    TimeUnit unit() default TimeUnit.MICROSECONDS;

    /**
     * 持有时间
     * @return
     */
    int time() default 5000;

    /**
     * 是否公平锁，false:非公平锁，true:公平锁
     */
    boolean fair() default false;

    /**
     * 尝试获取锁的最大时间
     */
    int expire() default 1000;
}
