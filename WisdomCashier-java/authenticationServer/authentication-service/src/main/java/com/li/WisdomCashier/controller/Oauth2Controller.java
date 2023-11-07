package com.li.WisdomCashier.controller;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName Oauth2Controller
 * @Description TODO
 * @Author Nine
 * @Date 2023/11/7 13:04
 * @Version 1.0
 */

@RestController
@RequestMapping("/oauth")
public class Oauth2Controller {

    @RequestMapping("/callback")
    public Object login(AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest();
        return authRequest.login(callback);
    }

    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("*")
                .clientSecret("*")
                .redirectUri("http://localhost:9700/oauth/callback")
                .build());
    }

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    @GetMapping("test")
    public String test(){
        return "pong";
    }
}
