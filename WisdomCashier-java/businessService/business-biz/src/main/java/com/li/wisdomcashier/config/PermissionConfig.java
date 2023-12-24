package com.li.wisdomcashier.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @ClassName PermissionConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/15 13:10
 * @Version 1.0
 */
@Slf4j
public class PermissionConfig implements PermissionEvaluator {
    @Override
    @SneakyThrows
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        if (authentication == null)
            return false;
        if (o == null || o1 == null)
            return true;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(null != authorities && (authorities.contains(new SimpleGrantedAuthority(o+o1.toString())))){
                return true;

        }
        log.warn("用户{}正试图访问无权限接口",authentication.getName());
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
