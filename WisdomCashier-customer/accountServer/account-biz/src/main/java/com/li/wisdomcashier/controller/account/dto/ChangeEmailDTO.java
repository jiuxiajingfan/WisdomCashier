package com.li.wisdomcashier.controller.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @ClassName changeEmailDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/11/5 21:10
 * @Version 1.0
 */
@Data
public class ChangeEmailDTO {
    @NotBlank(message = "必须填写新邮箱")
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "邮箱格式不正确！")
    String email;
    @NotBlank(message = "必须填写验证码")
    @Length(min = 6, max = 6, message = "验证码错误！")
    String code;
}
