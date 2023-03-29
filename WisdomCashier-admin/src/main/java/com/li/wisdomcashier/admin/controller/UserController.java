package com.li.wisdomcashier.admin.controller;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.service.AdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/29 14:49
 * @Version 1.0
 */

@RestController
@RequestMapping("/user")
@Api(tags = {"用户相关"})
public class UserController {
    @Resource
    private AdminUserService userService;



    @PostMapping("/getUser")
    @ApiOperation(value = "获取当前用户信息")
    public R<UserVo> getUser() {
        return userService.getUser();
    }
}
