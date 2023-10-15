package com.li.WisdomCashier.config;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * @ClassName PermissionConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/15 13:10
 * @Version 1.0
 */
public class PermissionConfig implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        if(authentication == null)
            return false;
        if(o == null || o1 == null)
           return true;
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
