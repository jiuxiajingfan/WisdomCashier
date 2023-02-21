package com.li.wisdomcashier.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName RoleEnum
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/21 14:09
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN(1,"系统管理员"),
    SHOPADMIN(2,"店铺管理员"),
    SHOP(3,"店铺用户");


    private int code;

    private String description;
}
