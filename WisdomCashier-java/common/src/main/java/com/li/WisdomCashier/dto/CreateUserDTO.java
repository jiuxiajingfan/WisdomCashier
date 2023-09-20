package com.li.WisdomCashier.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName CreateUserDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/11 19:11
 * @Version 1.0
 */
@Data
public class CreateUserDTO implements Serializable {
    @NotBlank(message = "邮箱不能为空！")
    String email;
    @NotBlank(message = "密码不能为空！")
    String password;
    @NotBlank(message = "验证码不能为空！")
    String code;
}
