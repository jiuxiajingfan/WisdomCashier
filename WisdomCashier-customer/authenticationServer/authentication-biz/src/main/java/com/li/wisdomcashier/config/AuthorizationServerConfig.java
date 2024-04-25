package com.li.wisdomcashier.config;

import com.li.wisdomcashier.authentication.AuthorizeService;
import com.li.wisdomcashier.authentication.DeviceClientAuthenticationConverter;
import com.li.wisdomcashier.authentication.DeviceClientAuthenticationProvider;
import com.li.wisdomcashier.authentication.password.PasswordGrantAuthenticationConverter;
import com.li.wisdomcashier.authentication.password.PasswordGrantAuthenticationProvider;
import com.li.wisdomcashier.exception.MyAuthenticationEntryPoint;
import com.li.wisdomcashier.utils.AccessTokenUtils;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.time.Instant;
import java.util.Date;

@Configuration
@Slf4j
public class AuthorizationServerConfig {

    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    @Resource
    private AuthorizeService userDetailsService;


    @Value("${authorization.rsa.private}")
    private String accessTokenRsaPrivateKey;

    /**
     * Spring Authorization Server 相关配置
     * 此处方法与下面defaultSecurityFilterChain都是SecurityFilterChain配置，配置的内容有点区别，
     * 因为Spring Authorization Server是建立在Spring Security 基础上的，defaultSecurityFilterChain方法主要
     * 配置Spring Security相关的东西，而此处authorizationServerSecurityFilterChain方法主要配置OAuth 2.1和OpenID Connect 1.0相关的东西
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http, RegisteredClientRepository registeredClientRepository,
                                                                      AuthorizationServerSettings authorizationServerSettings,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      OAuth2TokenGenerator<?> tokenGenerator)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        //AuthenticationConverter(预处理器)，尝试从HttpServletRequest提取客户端凭据,用以构建OAuth2ClientAuthenticationToken实例。
        DeviceClientAuthenticationConverter deviceClientAuthenticationConverter =
                new DeviceClientAuthenticationConverter(
                        authorizationServerSettings.getDeviceAuthorizationEndpoint());
        //AuthenticationProvider(主处理器)，用于验证OAuth2ClientAuthenticationToken。
        DeviceClientAuthenticationProvider deviceClientAuthenticationProvider =
                new DeviceClientAuthenticationProvider(registeredClientRepository);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .deviceAuthorizationEndpoint(deviceAuthorizationEndpoint ->
                        //设置用户码校验地址
                        deviceAuthorizationEndpoint.verificationUri("/activate")
                )
                .deviceVerificationEndpoint(deviceVerificationEndpoint ->
                        //设置授权页地址
                        deviceVerificationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI)
                )
                .clientAuthentication(clientAuthentication ->
                        //设置AuthenticationConverter(预处理器)和AuthenticationProvider(主处理器)
                        clientAuthentication
                                .authenticationConverter(deviceClientAuthenticationConverter)
                                .authenticationProvider(deviceClientAuthenticationProvider)
                )
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
                //设置自定义密码模式
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                .accessTokenRequestConverter(
                                        new PasswordGrantAuthenticationConverter())
                                .authenticationProvider(
                                        new PasswordGrantAuthenticationProvider(
                                                authorizationService, tokenGenerator)))
                //开启OpenID Connect
                .oidc(Customizer.withDefaults());

        //设置登录地址，需要进行认证的请求被重定向到该地址
        http
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                        .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }


    /**
     * 客户端信息
     * 对应表：oauth2_registered_client
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }


    /**
     * 授权信息
     * 对应表：oauth2_authorization
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 授权确认
     * 对应表：oauth2_authorization_consent
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置jwt解析器
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 配置认证服务器请求地址
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        //什么都不配置，则使用默认地址
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 配置token生成器
     */
    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer());
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwtClaimsSet.Builder claims = context.getClaims();
            UserDetails userDetails = userDetailsService.loadUserByUsername(context.getPrincipal().getName());
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                //给 AccessToken 添加签名信息
                claims.claim("authorities", userDetails.getAuthorities());
                AccessTokenUtils.signAccessToken(claims, accessTokenRsaPrivateKey);
            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                // Customize headers/claims for id_token
                claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()));
                StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
                claims.claim("sid", standardSessionIdGenerator.generateSessionId());
            }
        };
    }
}