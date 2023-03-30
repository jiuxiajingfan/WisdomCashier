package com.li.wisdomcashier.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.DeleteDTO;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.service.AdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


    @PostMapping("/getUserPage")
    @ApiOperation(value = "获取当前用户列表")
    public R<IPage<UserVo>> getUserPage(@Validated @RequestBody ShopQueryDTO queryDTO){
        return userService.getUserPage(queryDTO);
    }

    @PostMapping("/changeUserStatus")
    @ApiOperation(value = "更改用户状态")
    public R<String> changeUserStatus(@Validated @RequestBody DeleteDTO deleteDTO){
        return userService.changeUserStatus(deleteDTO);
    }
}
