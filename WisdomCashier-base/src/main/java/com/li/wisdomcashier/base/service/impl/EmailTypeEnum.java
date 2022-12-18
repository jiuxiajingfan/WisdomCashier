package com.li.wisdomcashier.base.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName EmailTypeEnum
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/18 14:49
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum EmailTypeEnum {

    signUp(0,"您正在进行注册服务"),
    changeEmail(1,"您正在进行修改绑定邮箱服务"),
    changePassword(2,"您正在进行修改密码服务"),
    findPassword(3,"您正在进行找回密码服务");
    private Integer value;
    private String label;
}
