package com.li.WisdomCashier.controller;

import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName EmailController
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/21 16:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/email")
public class EmailController {
    @Resource
    private EmailService emailService;

    @GetMapping("/getCode")
    public R<String> getCode(@RequestParam("email") String email,@RequestParam("type") Integer type){
        return emailService.getCode(email,type);
    }


}
