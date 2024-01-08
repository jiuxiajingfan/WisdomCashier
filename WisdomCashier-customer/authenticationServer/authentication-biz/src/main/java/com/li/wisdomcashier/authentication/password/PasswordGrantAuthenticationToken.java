package com.li.wisdomcashier.authentication.password;

import com.li.wisdomcashier.authentication.constant.OAuth2Constant;
import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

public class  PasswordGrantAuthenticationToken  extends OAuth2AuthorizationGrantAuthenticationToken {
    protected PasswordGrantAuthenticationToken(Authentication clientPrincipal,
                                               @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2Constant.GRANT_TYPE_PASSWORD),
                clientPrincipal, additionalParameters);
    }
}
