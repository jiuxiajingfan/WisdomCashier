package com.li.WisdomCashier.enums.news;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName NewsType
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/27 20:30
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum NewsTypeEnum {
    NEWS(0,"新闻"),
    ACTIVITY(1,"活动");
    private Integer value;
    private String desc;
}
