package com.li.wisdomcashier.base.bean;

import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.service.UserService;
import com.li.wisdomcashier.base.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * @ClassName MyRealm
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/17 21:37
 * @Version 1.0
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LogManager.getLogger(UserRealm.class);

    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Claims claim = jwtUtils.getClaimByToken(principals.toString());
        String username = claim.getSubject();
        UserBean user = userService.getUser(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(user.getRole());
        simpleAuthorizationInfo.addStringPermissions(user.getPermission());
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        log.info("==========================认证");
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        Claims claim = jwtUtils.getClaimByToken(auth.getPrincipal().toString());
        String username = claim.getSubject();
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        UserBean userBean = userService.getUser(username);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        return new SimpleAuthenticationInfo(token, token, getName());
    }

}
