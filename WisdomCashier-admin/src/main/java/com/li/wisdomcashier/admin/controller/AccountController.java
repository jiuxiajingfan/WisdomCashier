package com.li.wisdomcashier.admin.controller;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.UnCheck;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @Date 2023/3/28 16:16
 * @Version 1.0
 */

@RestController
@RequestMapping(value = {"/account"})
@Api(tags = {"登录相关"})
@Slf4j
public class AccountController {
    @Resource


    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @UnCheck
    public R<String> login(@Validated @RequestBody LoginDTO loginDto){
        return userService.login(loginDto);
    }
}
