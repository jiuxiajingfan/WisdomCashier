package com.li.wisdomcashier.base.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @ClassName LoginDto
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/18 21:02
 * @Version 1.0
 */
@Data
public class LoginDto {
    @ApiModelProperty(value = "用户账户")
    @NotBlank(message = "账号密码错误！登录失败！")
    @Length(min = 5, max = 20, message = "账号密码错误！登录失败！")
    String userName;

    //长度在6~18之间，只能包含字符、数字和下划线
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "账号密码错误！登录失败！")
    String userPwd;

    @ApiModelProperty(value = "图形验证信息")
    @NotBlank(message = "请先通过验证！")
    String verification;

}
