package com.li.wisdomcashier.enums.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ApplyEnum
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/20 13:15
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ApplyEnum {
    WAIT(1, "待审批"),
    SUCCESS(2, "通过"),
    REFUSE(3, "拒绝"),
    CHANEL(4, "撤销");

    private int code;

    private String description;
}
