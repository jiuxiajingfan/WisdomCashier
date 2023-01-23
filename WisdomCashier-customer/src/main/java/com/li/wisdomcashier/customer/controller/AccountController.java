package com.li.wisdomcashier.customer.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.UnCheck;
import com.li.wisdomcashier.base.entity.dto.ChangeEmailDto;
import com.li.wisdomcashier.base.entity.dto.LoginDto;
import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AccountController
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/11 16:37
 * @Version 1.0
 */
@RestController
@RequestMapping(value = {"/account"})
@Api(tags = {"登录相关"})
@Slf4j
public class AccountController {
    @Resource
    private UserService userService;

    @Resource
    private CaptchaService captchaService;

    @ApiOperation(value = "注册")
    @PostMapping("/signup")
    @UnCheck
    public R<String> signUp(@Validated  @RequestBody SignUpDto signUpDto){
        return userService.signUp(signUpDto);
    }

    @ApiOperation(value = "邮箱验证码服务")
    @GetMapping("/getcode")
    @UnCheck
    public R<String> getCode(String email,int type){
        return userService.getCode(email,type);
    }

    @ApiOperation(value = "邮箱验证码服务需验证")
    @GetMapping("/getcodeAuth")
    public R<String> getCodeAuth(int type){
        return userService.getCodeAuth(type);
    }

    @ApiOperation(value = "Ping")
    @RequiresAuthentication
    @PostMapping("/test")
    public R<User> test(){
        return userService.test();
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @UnCheck
    public R<String> login(@Validated  @RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    @ApiOperation(value = "滑块验证check")
    @PostMapping("/check")
    @UnCheck
    public ResponseModel check(@RequestBody CaptchaVO captchaVO){
        return captchaService.check(captchaVO);
    }

    @ApiOperation(value = "滑块验证get")
    @PostMapping("/get")
    @UnCheck
    public ResponseModel get(@RequestBody CaptchaVO captchaVO){
        return captchaService.get(captchaVO);
    }


    @ApiOperation(value = "快捷登录")
    @PostMapping("/login2")
    @UnCheck
    public R<String> login2(@Validated  @RequestBody LoginDto loginDto){
        return userService.login2(loginDto);
    }

    @ApiOperation(value = "注销登录")
    @PostMapping("/loginOut")
    public R<String> loginOut(HttpServletRequest httpServletRequest){
        return userService.loginOut(httpServletRequest);
    }

    @ApiOperation(value = "修改绑定邮箱")
    @PostMapping("/changeUserEmail")
    public R<String> changeUserEmail(@RequestBody @Validated ChangeEmailDto changeEmailDto){
        return userService.changeUserEmail(changeEmailDto);
    }


}
