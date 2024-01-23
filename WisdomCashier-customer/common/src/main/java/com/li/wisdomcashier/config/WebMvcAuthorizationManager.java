package com.li.wisdomcashier.config;

import com.li.wisdomcashier.utils.AccessTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * 应用鉴权管理器
 */
public class WebMvcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("${authorization.rsa.public}")
    private String publicKey;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorizationToken)) {
            return new AuthorizationDecision(false);
        }
        boolean verifyResult = AccessTokenUtils.verifyAccessToken(authorizationToken, publicKey);

        if (!verifyResult) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }
}