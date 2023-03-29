package com.li.wisdomcashier.admin.controller;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.UnCheck;
import com.li.wisdomcashier.base.entity.dto.LoginDTO;
import com.li.wisdomcashier.base.entity.po.AdminUser;
import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.entity.vo.EChartVO;
import com.li.wisdomcashier.base.service.AdminUserService;
import com.li.wisdomcashier.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    AdminUserService adminUserService;

    @Resource
    UserService userService;


    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @UnCheck
    public R<String> login(@Validated @RequestBody LoginDTO loginDto) {
        return adminUserService.login(loginDto);
    }

    @ApiOperation(value = "Ping")
    @PostMapping("/test")
    public R<AdminUser> test(){
        return adminUserService.test();
    }

    @ApiOperation(value = "注销登录")
    @PostMapping("/loginOut")
    public R<String> loginOut(HttpServletRequest httpServletRequest){
        return adminUserService.loginOut(httpServletRequest);
    }

    @ApiOperation(value = "请求菜单")
    @GetMapping("/getAdminCenterMenu")
    public  R<List<SysMenu>> getAdminCenterMenu(){
        return userService.getAdminCenterMenu();
    }

    @ApiOperation(value = "注销登录")
    @GetMapping("/getSystem")
    public R<List<List<EChartVO>>>  getSystem() {
        return adminUserService.getSystem();
    }
}




