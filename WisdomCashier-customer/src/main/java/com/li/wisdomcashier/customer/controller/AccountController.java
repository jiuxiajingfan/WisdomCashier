package com.li.wisdomcashier.customer.controller;

import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.R;
import com.li.wisdomcashier.base.service.EmailService;
import com.li.wisdomcashier.base.service.UserService;
import com.li.wisdomcashier.base.util.RedisUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName AccountController
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/11 16:37
 * @Version 1.0
 */
@RestController
@RequestMapping(value = {"/account"})
@Api(tags = {"账户相关"})
@Slf4j
public class AccountController {
    @Resource
    private UserService userService;

    @Resource
    private EmailService emailService;

    @Resource
    private RedisUtils redisUtils;

    @PostMapping("/signup")
    public R<String> signUp(@Validated  @RequestBody SignUpDto signUpDto){
        return userService.signUp(signUpDto);
    }

    @GetMapping("/getcode")
    public R<String> getCode(String email){
        return emailService.getCode(email);
    }
}
