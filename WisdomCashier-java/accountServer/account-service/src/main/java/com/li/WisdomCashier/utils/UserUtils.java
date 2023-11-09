package com.li.WisdomCashier.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.WisdomCashier.mapper.UserMapper;
import com.li.WisdomCashier.po.User;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
