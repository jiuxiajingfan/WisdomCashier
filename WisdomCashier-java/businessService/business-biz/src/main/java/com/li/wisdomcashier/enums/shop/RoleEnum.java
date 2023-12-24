package com.li.wisdomcashier.enums.shop;

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
    SHOPMASTER(1,"店主"),
    SHOPADMIN(2,"店铺管理员"),
    SHOP(3,"店铺用户"),

    GUEST(4,"普通用户");


    private int code;

    private String description;
}
