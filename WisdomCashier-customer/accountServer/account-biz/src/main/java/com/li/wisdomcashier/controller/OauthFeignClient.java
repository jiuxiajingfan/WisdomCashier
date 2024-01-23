package com.li.wisdomcashier.controller;

import org.springframework.cloud.openfeign.FeignClient;
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
    @PostMapping(value = "/oauth2/token", headers = {"Authorization=Basic cGFzc3dvcmQtY2xpZW50LWlkOnNlY3JldA=="})
    String postAccessToken(@RequestParam("grant_type") String grantType,
                           @RequestParam("username") String userName,
                           @RequestParam("password") String password,
                           @RequestParam("scope") String scope);

    @PostMapping("/oauth2/check_token")
    Map<String, ?> checkToken(@RequestParam("token") String value);
}
