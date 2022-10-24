package com.li.wisdomcashier.customer.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.LoginDto;
import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.JWTToken;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
    private CaptchaService captchaService;

    @ApiOperation(value = "注册")
    @PostMapping("/signup")
    public R<String> signUp(@Validated  @RequestBody SignUpDto signUpDto){
        return userService.signUp(signUpDto);
    }

    @ApiOperation(value = "邮箱验证码服务")
    @GetMapping("/getcode")
    public R<String> getCode(String email){
        return userService.getCode(email);
    }

    @ApiOperation(value = "Ping")
    @RequiresAuthentication
    @GetMapping("/test")
    public R<User> test(){
        return userService.test();
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R<String> login(@Validated  @RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    @ApiOperation(value = "滑块验证")
    @PostMapping("/check")
    public ResponseModel check(@RequestBody CaptchaVO captchaVO){
        return captchaService.check(captchaVO);
    }


}
