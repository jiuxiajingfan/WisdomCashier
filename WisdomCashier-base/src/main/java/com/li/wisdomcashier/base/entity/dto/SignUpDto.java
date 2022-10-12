package com.li.wisdomcashier.base.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @ClassName SignUpDto
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/11 16:45
 * @Version 1.0
 */
@ApiOperation(value = "注册dto")
@Data
public class SignUpDto {
    @ApiModelProperty(value = "用户账户")
    @Length(min = 5,max = 20,message = "账号长度必须在5-20位之间！")
    String userName;

    @ApiModelProperty(value = "用户名")
    @Length(min = 1,max = 8,message = "用户名必须在1-8位之间！" )
    String userNickname;

//   ，长度在6~18之间，只能包含字符、数字和下划线
    @ApiModelProperty(value = "密码")
    @Pattern(regexp = "\\w{6,18}$" , message = "密码格式不符合要求")
    String userPwd;

    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "邮箱格式不正确！")
    String email;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^\\d{11}$",message = "11位手机号错误！")
    String phone;

    @ApiModelProperty(value = "验证码")
    @Length(min = 6,max = 6,message = "验证码错误！")
    String code;
}
