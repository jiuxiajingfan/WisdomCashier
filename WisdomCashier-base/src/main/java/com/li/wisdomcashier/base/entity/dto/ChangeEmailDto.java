package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @ClassName EmailDto
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/18 17:07
 * @Version 1.0
 */
@Data
public class ChangeEmailDto {
    @NotBlank(message = "必须填写新邮箱")
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "邮箱格式不正确！")
    String email;
    @NotBlank(message = "必须填写验证码")
    @Length(min = 6,max = 6,message = "验证码错误！")
    String code;
}
