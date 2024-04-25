package com.li.wisdomcashier.config;

import com.li.wisdomcashier.service.CaptchaCacheService;
import com.li.wisdomcashier.service.impl.CaptchaServiceFactory;
import com.li.wisdomcashier.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @ClassName CaptchaCacheServiceRedisImpl
 * @Description Redis存储登录校验RedisUtils信息
 * @Author Nine
 * @Date 2023/7/25 15:46
 * @Version 1.0
 */
@Slf4j
@Service
public class CaptchaCacheServiceRedisImpl implements CaptchaCacheService {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        //开源组件存在设计缺陷
        if (value.compareTo("1") == 0) {
            redisUtils.set(key, 1, expiresInSeconds);
        } else {
            redisUtils.set(key, value, expiresInSeconds);
        }
    }

    @Override
    public boolean exists(String key) {
        return redisUtils.hasKey(key);
    }

    @Override
    public void delete(String key) {
        redisUtils.del(key);
    }

    @Override
    public String get(String key) {
        return redisUtils.get(key).toString();
    }

    @Override
    public String type() {
        return "redis";
    }

    @Override
    @PostConstruct
    public void init() {
        CaptchaServiceFactory.put("redis", this);
    }

    @Override
    public Long increment(String key, long val) {
        return redisUtils.incr(key, val);
    }
}
