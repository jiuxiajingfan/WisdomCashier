package com.li.WisdomCashier.controller;

import com.li.WisdomCashier.dto.CreateUserDTO;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.UserService;
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
 * @Date 2023/9/21 16:26
 * @Version 1.0
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    private UserService userService;

    @PostMapping("createUser")
    R<String> createUser(@RequestBody @Validated CreateUserDTO createUserDTO){
        return userService.createUser(createUserDTO);
    };
}
