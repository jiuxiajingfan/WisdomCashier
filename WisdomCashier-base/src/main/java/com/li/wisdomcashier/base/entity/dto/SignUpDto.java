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
    @NotBlank(message = "账号不能为空")
    @Length(min = 5,max = 20,message = "账号长度必须在5-20位之间！")
    String userName;

//   长度在6~18之间，只能包含字符、数字和下划线
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    String userPwd;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "邮箱格式不正确！")
    String email;

//    @ApiModelProperty(value = "手机号")
//    @Pattern(regexp = "^\\d{11}$",message = "11位手机号错误！")
//    String phone;

    @ApiModelProperty(value = "验证码")
    @Length(min = 6,max = 6,message = "验证码错误！")
    String code;
}
