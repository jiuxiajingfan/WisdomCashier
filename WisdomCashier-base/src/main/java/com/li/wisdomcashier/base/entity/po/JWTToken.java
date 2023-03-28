package com.li.wisdomcashier.base.entity.po;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;
    private String type;

    public JWTToken(String token, String type) {
        this.token = token;
        this.type = type;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getLoginType() {

        return type;
    }

    public void setLoginType(String loginType) {

        this.type = loginType;

    }
}
