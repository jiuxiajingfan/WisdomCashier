package com.li.wisdomcashier.base.util;

import java.util.Random;

/**
 * @ClassName CodeUtils
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/12 17:17
 * @Version 1.0
 */
public class CodeUtils {
    private static String code;

    public static String getCode(){
        Random random = new Random();
        code="";
        for(int i=0;i<6;i++){
            code+=String.valueOf(random.nextInt(10));
        }
        return code;
    }
}
