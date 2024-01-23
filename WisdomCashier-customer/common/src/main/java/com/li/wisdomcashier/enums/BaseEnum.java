package com.li.wisdomcashier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName BaseEnum
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/27 20:27
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum BaseEnum {
    FALSE(1, "false"),
    TRUE(1, "true");
    private Integer value;
    private String desc;
}
