package com.li.WisdomCashier.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName PermissionConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/15 13:10
 * @Version 1.0
 */
@Slf4j
public class PermissionConfig implements PermissionEvaluator {

    ObjectMapper mapper = new ObjectMapper();

    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    @Override
    @SneakyThrows
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        if (authentication == null)
            return false;
        if (o == null || o1 == null)
            return true;
        OAuth2AuthenticationDetails details =(OAuth2AuthenticationDetails) authentication.getDetails();
        String claims = JwtHelper.decode(details.getTokenValue()).getClaims();
        Map<String, Object> parse = mapper.readValue(claims,Map.class);
        String authorities = writer.writeValueAsString(parse.getOrDefault("authorities", null));
        if(null != authorities && (authorities.contains(o +o1.toString()))){
            return true;

        }
        log.warn("用户{}正试图访问无权限接口",parse.get("user_name"));
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
