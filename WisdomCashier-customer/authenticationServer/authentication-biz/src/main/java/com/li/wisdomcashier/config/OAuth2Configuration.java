package com.li.wisdomcashier.config;


import com.li.wisdomcashier.error.MssWebResponseExceptionTranslator;
import com.li.wisdomcashier.service.AuthorizeService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;

/**
 * @ClassName OauthConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/6/12 13:33
 * @Version 1.0
 */
@EnableAuthorizationServer   //开启验证服务器
@Configuration
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

   private Integer JWTTime = 10800;

    @Resource
    private AuthenticationManager manager;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    TokenStore store;

    @Resource
    JwtAccessTokenConverter converter;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final MssWebResponseExceptionTranslator exceptionTranslator = new MssWebResponseExceptionTranslator();

    private AuthorizationServerTokenServices serverTokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setSupportRefreshToken(true);
        services.setTokenStore(store);
        services.setTokenEnhancer(converter);
        services.setAccessTokenValiditySeconds(JWTTime);
        services.setRefreshTokenValiditySeconds(JWTTime);
        return services;
    }


    /**
     * 这个方法是对客户端进行配置，一个验证服务器可以预设很多个客户端，
     * 之后这些指定的客户端就可以按照下面指定的方式进行验证
     * @param clients 客户端配置工具
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()   //这里我们直接硬编码创建，当然也可以像Security那样自定义或是使用JDBC从数据库读取
                .withClient("gateway")   //客户端名称，随便起就行
                .secret(encoder.encode("jiuxiajingfan"))      //只与客户端分享的secret，随便写，但是注意要加密
                .autoApprove(true)    //自动审批，这里关闭，要的就是一会体验那种感觉
                .scopes("all")     //授权范围，这里我们使用全部all
                .redirectUris("https://www.baidu.com")
                .authorizedGrantTypes("client_credentials", "password", "implicit", "authorization_code", "refresh_token");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .passwordEncoder(encoder)    //编码器设定为BCryptPasswordEncoder
                .allowFormAuthenticationForClients()  //允许客户端使用表单验证，一会我们POST请求中会携带表单信息
                .checkTokenAccess("permitAll()");     //允许所有的Token查询请求
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .exceptionTranslator(exceptionTranslator)
                .tokenServices(serverTokenServices())
                .userDetailsService(authorizeService)
                .authenticationManager(manager);
    }

}