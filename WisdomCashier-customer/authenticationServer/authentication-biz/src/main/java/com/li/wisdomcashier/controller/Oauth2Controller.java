package com.li.wisdomcashier.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
