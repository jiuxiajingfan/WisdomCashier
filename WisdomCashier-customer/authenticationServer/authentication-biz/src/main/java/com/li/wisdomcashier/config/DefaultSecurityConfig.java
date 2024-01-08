package com.li.wisdomcashier.config;

import com.li.wisdomcashier.authentication.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

    @Resource
    private AuthorizeService authorizeService;

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/actuator/**"),
                                new AntPathRequestMatcher("/oauth2/**"),
                                new AntPathRequestMatcher("/**/*.json"),
                                new AntPathRequestMatcher("/**/*.html")).permitAll()
                        .anyRequest().authenticated()
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
				// Form login handles the redirect to the login page from the
				// authorization server filter chain
                .formLogin(Customizer.withDefaults())
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return authorizeService;
    }

}