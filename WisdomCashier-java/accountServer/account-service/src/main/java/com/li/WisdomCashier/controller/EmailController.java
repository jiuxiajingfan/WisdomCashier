package com.li.WisdomCashier.controller;

import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

/**
 * @ClassName EmailController
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/21 16:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/email")
@Api(tags = "邮件相关")
public class EmailController {
    @Resource
    private EmailService emailService;

    @GetMapping("/getCode")
    @ApiOperation("获取邮件")
    @PermitAll
    public R<String> getCode(@RequestParam("email") @ApiParam("邮箱") String email,
                             @RequestParam("type") @ApiParam("类型") Integer type){
        return emailService.getCode(email,type);
    }


}
