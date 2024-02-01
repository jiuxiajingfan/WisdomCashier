package com.li.wisdomcashier.authentication.password;

import com.li.wisdomcashier.authentication.AuthorizeService;
import com.li.wisdomcashier.exception.MyAuthenticationException;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ERROR_URI;


@Slf4j
public class PasswordGrantAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private AuthorizeService userDetailsService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public PasswordGrantAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                               OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordGrantAuthenticationToken passwordGrantAuthenticationToken = (PasswordGrantAuthenticationToken) authentication;

        Map<String, Object> additionalParameters = passwordGrantAuthenticationToken.getAdditionalParameters();
        //授权类型
        AuthorizationGrantType authorizationGrantType = passwordGrantAuthenticationToken.getGrantType();
        //用户名
        String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
        //密码
        String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
        //请求参数权限范围
        String requestScopesStr = (String) additionalParameters.get(OAuth2ParameterNames.SCOPE);
        if (StringUtils.isBlank(requestScopesStr)) {
            throw new MyAuthenticationException("缺少权限参数scope！");
        }
        //请求参数权限范围专场集合
        Set<String> requestScopeSet = Stream.of(requestScopesStr.split(" ")).collect(Collectors.toSet());


        // Ensure the client is authenticated
        OAuth2ClientAuthenticationToken clientPrincipal =
                getAuthenticatedClientElseThrowInvalidClient(passwordGrantAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // Ensure the client is configured to use this authorization grant type
        if (!registeredClient.getAuthorizationGrantTypes().contains(authorizationGrantType)) {
            throw new MyAuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        //校验用户名信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new MyAuthenticationException("用户名或密码错误！");
        }

        //由于在上面已验证过用户名、密码，现在构建一个已认证的对象UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(), clientPrincipal, userDetails.getAuthorities());

        // Initialize the DefaultOAuth2TokenContext
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(authorizationGrantType)
                .authorizedScopes(requestScopeSet)
                .authorizationGrant(passwordGrantAuthenticationToken);

        // Initialize the OAuth2Authorization
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizedScopes(requestScopeSet)
                .attribute(Principal.class.getName(), usernamePasswordAuthenticationToken)
                .authorizationGrantType(authorizationGrantType);

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new MyAuthenticationException(error.toString());
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new MyAuthenticationException(error.toString());
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        //保存认证信息
        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, accessToken, refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new MyAuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}