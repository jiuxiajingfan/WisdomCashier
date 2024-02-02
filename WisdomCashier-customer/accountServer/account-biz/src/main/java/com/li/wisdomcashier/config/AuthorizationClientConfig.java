package com.li.wisdomcashier.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.li.wisdomcashier.exception.MyAuthenticationEntryPoint;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthorizationClientConfig {

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    WebMvcAuthorizationManager webMvcAuthorizationManager() {
        return new WebMvcAuthorizationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        Multimap<HttpMethod, String> permitAllUrls = getPermitAllUrlsFromAnnotations();
        // 禁用basic明文验证
        http.httpBasic(AbstractHttpConfigurer::disable)
                // 前后端分离架构不需要csrf保护
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用默认登录页
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用默认登出页
                .logout(AbstractHttpConfigurer::disable)
                // 前后端分离是无状态的，不需要session了，直接禁用。
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests
                        (authorize ->
                                authorize
                                        //swagger
                                        .requestMatchers(HttpMethod.GET, "/v3/api-docs","/v3/api-docs/**", "/swagger-resources","/swagger-resources/**").permitAll()
                                        //静态资源
                                        .requestMatchers(HttpMethod.GET, "/**.html",  "/**.css", "/**.js", "/**.ico").permitAll()
                                        //放行@Permit注解
                                        .requestMatchers(HttpMethod.GET, permitAllUrls.get(HttpMethod.GET).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.POST, permitAllUrls.get(HttpMethod.POST).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.PUT, permitAllUrls.get(HttpMethod.PUT).toArray(new String[0])).permitAll()
                                        .requestMatchers(HttpMethod.DELETE, permitAllUrls.get(HttpMethod.DELETE).toArray(new String[0])).permitAll()
                                        // 鉴权管理器配置
                                        .anyRequest().access(webMvcAuthorizationManager())
                        )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                );
        return http.build();
    }

    @Bean("ss")
    PermissionConfig permissionConfig() {
        return new PermissionConfig();
    }


    private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
        Multimap<HttpMethod, String> result = HashMultimap.create();
        // 获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @PermitAll 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class) || null == entry.getKey().getPathPatternsCondition()) {
                continue;
            }
            Set<String> urls = entry.getKey().getPathPatternsCondition().getPatterns().stream()
                    .map(PathPattern::toString).collect(Collectors.toSet());
            // 根据请求方法，添加到 result 结果
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        result.putAll(HttpMethod.GET, urls);
                        break;
                    case POST:
                        result.putAll(HttpMethod.POST, urls);
                        break;
                    case PUT:
                        result.putAll(HttpMethod.PUT, urls);
                        break;
                    case DELETE:
                        result.putAll(HttpMethod.DELETE, urls);
                        break;
                    default:
                        break;
                }
            });
        }
        return result;
    }
}