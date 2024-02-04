package com.li.wisdomcashier.controller.email;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName EmailController
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/21 16:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/email")
@Tag(name = "邮件相关")
public class EmailController {
    @Resource
    private EmailService emailService;

    @GetMapping("/getCode")
    @Operation(summary = "获取邮件")
    @PermitAll
    public R<String> getCode(@RequestParam("email")
//                                 @ApiParam("邮箱")
                             String email,
                             @RequestParam("type")
//                             @ApiParam("类型")
                             Integer type) {
        return emailService.getCode(email, type);
    }


}
