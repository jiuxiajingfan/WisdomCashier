package com.li.wisdomcashier.customer.controller;

import com.li.wisdomcashier.base.entity.dto.SignUpDto;
import com.li.wisdomcashier.base.entity.po.R;
import com.li.wisdomcashier.base.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/signup")
    public R<String> signUp(@Validated  @RequestBody SignUpDto signUpDto){
        return userService.signUp(signUpDto);
    }
}
