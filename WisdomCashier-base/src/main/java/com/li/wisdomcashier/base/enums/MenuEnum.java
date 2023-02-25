package com.li.wisdomcashier.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName MenuEnum
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/25 20:41
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum MenuEnum {
    USERCENTER(1,"个人中心"),
    SHOPMENU(2,"店铺菜单");
    private int code;

    private String description;
}
