package com.li.wisdomcashier.base.bean;

import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.service.AdminUserService;
import com.li.wisdomcashier.base.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * @ClassName AdminRealm
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/28 19:17
 * @Version 1.0
 */
public class AdminRealm extends AuthorizingRealm {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private AdminUserService adminUserService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    public void setName(String name){
        super.setName("AdminRealm");
    }
    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Claims claim = jwtUtils.getClaimByToken(principals.toString());
        String username = claim.getSubject();
        UserBean user = adminUserService.getUser(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(user.getRole());
        simpleAuthorizationInfo.addStringPermissions(user.getPermission());
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException{
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        Claims claim = jwtUtils.getClaimByToken(auth.getPrincipal().toString());
        String username = claim.getSubject();
        if (username == null) {
            throw new AuthenticationException("不存在该用户！");
        }
        UserBean userBean = adminUserService.getUser(username);
        if (userBean == null) {
            throw new AuthenticationException("不存在该用户！");
        }
        if(userBean.getStatus()== 1){
            throw new AuthenticationException("该用户已被封禁，请联系管理员解封！");
        }
        if(userBean.getStatus() == 2)
        {
            throw new AuthenticationException("该用户已注销！");
        }

        return new SimpleAuthenticationInfo(token, token, getName());
    }

}
