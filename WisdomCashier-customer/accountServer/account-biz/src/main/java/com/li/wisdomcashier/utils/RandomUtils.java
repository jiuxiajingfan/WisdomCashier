//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public RandomUtils() {
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }

    public static String getRandomHan(String hanZi) {
        String ch = hanZi.charAt((new Random()).nextInt(hanZi.length())) + "";
        return ch;
    }

    public static int getRandomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static String getRandomHan() {
        String str = "";
        Random random = new Random();
        int highCode = 176 + Math.abs(random.nextInt(39));
        int lowCode = 161 + Math.abs(random.nextInt(93));
        byte[] b = new byte[]{Integer.valueOf(highCode).byteValue(), Integer.valueOf(lowCode).byteValue()};

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException var6) {
            UnsupportedEncodingException e = var6;
            e.printStackTrace();
        }

        return str;
    }

    public static Integer getRandomInt(int startNum, int endNum) {
        return ThreadLocalRandom.current().nextInt(endNum - startNum) + startNum;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        return sb.toString();
    }
}
