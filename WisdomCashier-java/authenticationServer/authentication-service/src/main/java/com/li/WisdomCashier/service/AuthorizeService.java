package com.li.WisdomCashier.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.WisdomCashier.mapper.UserMapper;
import com.li.WisdomCashier.po.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName AuthorizeService
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/10 13:46
 * @Version 1.0
 */
@Component
public class AuthorizeService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserName, userName));
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户名或密码错误！");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getUserPwd())
                .roles("USER")
                .build();
    }
}
