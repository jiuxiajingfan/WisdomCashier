package com.li.wisdomcashier.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.entry.User;
import com.li.wisdomcashier.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName UserUtils
 * @Description TODO
 * @Author Nine
 * @Date 2023/11/5 20:45
 * @Version 1.0
 */
@Component
public class UserUtils {
    @Resource
    private UserMapper userMapper;

    private static UserUtils userUtils;

    @Resource
    private RedisUtils redisUtils;


    @PostConstruct
    public void init() {
        userUtils = this;
        userUtils.userMapper = this.userMapper;
        userUtils.redisUtils = this.redisUtils;
    }

    @SneakyThrows
    public static User getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userUtils.userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserName, userName));
    }
}
