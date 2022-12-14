package com.li.wisdomcashier.base.util;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ClassName UserUtils
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/2 14:10
 * @Version 1.0
 */
@Component
public class UserUtils {

    @Resource
    private UserMapper userMapper;

    private static UserUtils userUtils;


    @PostConstruct
    public void init(){
        userUtils = this;
        userUtils.userMapper = this.userMapper;
    }

    public static User getUser(){
        JwtUtils jwtUtils = new JwtUtils();
        Subject subject = SecurityUtils.getSubject();
        Claims claimByToken = jwtUtils.getClaimByToken(subject.getPrincipal().toString());
        User user = userUtils.userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, claimByToken.getSubject()));
        System.out.println("hello");
        return  user;
    }
}
