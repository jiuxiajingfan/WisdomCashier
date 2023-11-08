package com.li.WisdomCashier.controller;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
public class Oauth2Controller {
    @GetMapping("test")
    public String test(){
        return "pong";
    }
}
