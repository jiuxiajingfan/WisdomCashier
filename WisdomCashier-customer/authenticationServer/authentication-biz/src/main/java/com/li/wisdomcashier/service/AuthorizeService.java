package com.li.wisdomcashier.service;

import com.li.wisdomcashier.mapper.RoleMapper;
import com.li.wisdomcashier.mapper.UserMapper;
import com.li.wisdomcashier.po.Role;
import com.li.wisdomcashier.po.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

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

    @Resource
    private RoleMapper roleMapper;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userMapper.selectOne(lambdaQuery(User.class).eq(User::getUserName, userName));
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户名或密码错误！");
        }
        List<Role> roles = roleMapper.selectList(lambdaQuery(Role.class)
                .eq(Role::getUserId, user.getId()));
        ArrayList<SimpleGrantedAuthority> userRoles = new ArrayList<>();
        for (Role role : roles) {
                userRoles.add(new SimpleGrantedAuthority(role.getShopId().toString() + role.getRole()));
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getUserPwd())
                .authorities(userRoles)
                .build();
    }
}
