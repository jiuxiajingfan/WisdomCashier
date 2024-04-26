package com.li.wisdomcashier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName VipEnum
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/21 15:05
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum VipEnum {
    ACTIVE("有效",1),
    LIMITED("过期",2);

    private String desc;
    private Integer code;
}
