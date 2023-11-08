package com.li.WisdomCashier.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.li.WisdomCashier.mapper.UserMapper;
import com.li.WisdomCashier.po.User;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

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
    public void init(){
        userUtils = this;
        userUtils.userMapper = this.userMapper;
        userUtils.redisUtils = this.redisUtils;
    }

    @SneakyThrows
    public static User getUser(){
        ObjectMapper mapper = new ObjectMapper();

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details =(OAuth2AuthenticationDetails) authentication.getDetails();
        String claims = JwtHelper.decode(details.getTokenValue()).getClaims();

        Map<String, Object> parse = mapper.readValue(claims,Map.class);
        String userName = (String)parse.getOrDefault("user_name", null);
       return userUtils.userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserName, userName));
    }
}
