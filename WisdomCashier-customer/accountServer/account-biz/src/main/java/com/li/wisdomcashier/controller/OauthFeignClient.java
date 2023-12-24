package com.li.wisdomcashier.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @ClassName test
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/22 15:03
 * @Version 1.0
 */
@FeignClient("oauth2-server")
public interface OauthFeignClient {
    @PostMapping(value = "/oauth/token",headers = {"Authorization=Basic Z2F0ZXdheTpqaXV4aWFqaW5nZmFu"})
    OAuth2AccessToken postAccessToken(@RequestParam("grant_type")String grantType,
                                         @RequestParam("username")String userName,
                                         @RequestParam("password")String password);

    @PostMapping("/oauth/check_token")
    Map<String,?> checkToken(@RequestParam("token") String value);
}
