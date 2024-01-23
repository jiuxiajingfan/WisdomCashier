package com.li.wisdomcashier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class AuthorizationClientConfig {

    @Bean
    WebFluxAuthorizationManager webFluxAuthorizationManager() {
        return new WebFluxAuthorizationManager();
    }

    @Bean
    public SecurityWebFilterChain authorizationClientSecurityChain(ServerHttpSecurity http) throws Exception {
        return http
                .authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec
                            .anyExchange().access(webFluxAuthorizationManager());
                })
                .cors(Customizer.withDefaults())
                .csrf((csrf -> csrf.disable())).build();
    }

}