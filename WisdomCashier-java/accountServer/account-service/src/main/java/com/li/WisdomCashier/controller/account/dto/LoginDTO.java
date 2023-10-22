package com.li.WisdomCashier.controller.account.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空！")
    String username;
    @NotBlank(message = "密码不能为空！")
    String password;
    @NotBlank(message = "请先通过验证！")
    String verify;
}
