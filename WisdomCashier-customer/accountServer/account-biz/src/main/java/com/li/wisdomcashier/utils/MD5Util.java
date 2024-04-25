//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.utils;

import java.security.MessageDigest;

public abstract class MD5Util {
    public MD5Util() {
    }

    public static String md5(String dataStr) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte[] s = m.digest();
            StringBuilder result = new StringBuilder();

            for(int i = 0; i < s.length; ++i) {
                result.append(Integer.toHexString(255 & s[i] | -256).substring(6));
            }

            return result.toString();
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
            return "";
        }
    }

    public static String md5WithSalt(String dataStr, String salt) {
        return md5(dataStr + salt);
    }
}
