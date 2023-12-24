package com.li.wisdomcashier.controller.account.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @ClassName ChangePwdDto
 * @Description TODO
 * @Author Nine
 * @Date 2023/1/23 12:44
 * @Version 1.0
 */
@Data
public class ChangePwdDTO {
    @NotBlank(message = "原密码不能为空")
    @ApiModelProperty(value = "原始密码")
    String pwdOriginal;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码")
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{8,34}$", message = "密码必须包含数字、字母、特殊符号中的两种，长度为8-30位！")
    String pwdNew;

    @NotBlank(message = "确定新密码不能为空")
    @ApiModelProperty(value = "确认新密码")
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{8,34}$", message = "密码必须包含数字、字母、特殊符号中的两种，长度为8-30位！")
    String pwdConfirm;
}
