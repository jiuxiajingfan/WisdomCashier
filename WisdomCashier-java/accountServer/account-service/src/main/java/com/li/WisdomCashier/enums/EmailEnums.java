package com.li.WisdomCashier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName EmailEmums
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/20 16:24
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum EmailEnums {
    REGISTER(0,"注册验证码");
    private final Integer type;
    private final String desc;
    private static Map<Integer, EmailEnums> cache;
    static {
        cache = Arrays.stream(EmailEnums.values()).collect(Collectors.toMap(EmailEnums::getType, Function.identity()));
    }
    public static EmailEnums of(Integer type){
        return cache.get(type);
    }
}
