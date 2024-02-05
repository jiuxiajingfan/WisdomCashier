package com.li.wisdomcashier.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @ClassName PermissionConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/15 13:10
 * @Version 1.0
 */
@Slf4j
public class PermissionConfig {
    public boolean hasPermission(Long id, Integer... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return false;
        if (id == null || roles == null)
            return true;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (null != authorities) {
            for (Integer role : roles) {
                SimpleGrantedAuthority o = new SimpleGrantedAuthority(id + role.toString());
                if (authorities.contains(o)) {
                    return true;
                }
            }
        }
        log.warn("用户{}正试图访问无权限接口", authentication.getName());
        return false;
    }
}
