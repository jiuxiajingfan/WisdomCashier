package com.li.wisdomcashier.enums.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 移动支付方式Enums
 */
@Getter
@AllArgsConstructor
public enum PayEnums {
    ZFB(2, "支付宝"),
    WX(1, "微信"),
    XJ(0,"现金");


    private final Integer type;
    private final String value;
    private static Map<Integer, PayEnums> cache;

    static {
        cache = Arrays.stream(PayEnums.values()).collect(Collectors.toMap(PayEnums::getType, Function.identity()));
    }

    public static PayEnums of(Integer type) {
        return cache.get(type);
    }
}
