package com.li.WisdomCashier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/6/12 13:32
 * @Version 1.0
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //放行接口
                .antMatchers("/account/createUser").permitAll()
                .antMatchers("/account/login").permitAll()
                .antMatchers("/account/getCaptcha").permitAll()
                .antMatchers("/account/checkCaptcha").permitAll()
                .antMatchers("/email/getCode").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/doc.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Bean("ss")
    PermissionConfig permissionConfig(){
        return new PermissionConfig();
    }
}
