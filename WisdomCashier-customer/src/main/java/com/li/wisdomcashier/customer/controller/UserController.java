package com.li.wisdomcashier.customer.controller;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/4 21:20
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = {"用户相关"})
public class UserController {

    @Resource
    private UserService userService;



    @PostMapping("/getUser")
    @ApiOperation(value = "获取当前用户信息")
    public R<UserVo> getUser() {
        return userService.getUser();
    }

    @GetMapping("/changeUserNickName")
    @ApiOperation(value = "修改用户名")
    public R<String> changeUserNickName(String name) {
        return userService.changeUserNickName(name);
    }
}
