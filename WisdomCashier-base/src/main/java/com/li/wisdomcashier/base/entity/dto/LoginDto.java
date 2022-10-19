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
    @NotBlank(message = "账号不能为空")
    @Length(min = 5,max = 20,message = "账号长度必须在5-20位之间！")
    String userName;

    //   ，长度在6~18之间，只能包含字符、数字和下划线
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "\\w{6,18}$" , message = "密码格式不符合要求")
    String userPwd;

}
