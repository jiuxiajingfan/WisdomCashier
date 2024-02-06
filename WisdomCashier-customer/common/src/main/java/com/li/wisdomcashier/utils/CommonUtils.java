package com.li.wisdomcashier.utils;

/**
 * @ClassName CommonUtils
 * @Description TODO
 * @Author Nine
 * @Date 2023/11/6 12:29
 * @Version 1.0
 */

public class CommonUtils {
    public static boolean compare(Object a, Object b) {
        if(null == a || null == b){
            return false;
        }
        return a.equals(b);
    }
}
