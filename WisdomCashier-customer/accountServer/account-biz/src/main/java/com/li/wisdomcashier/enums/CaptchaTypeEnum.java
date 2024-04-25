//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.enums;

public enum CaptchaTypeEnum {
    BLOCKPUZZLE("blockPuzzle", "滑块拼图"),
    CLICKWORD("clickWord", "文字点选"),
    DEFAULT("default", "默认");

    private String codeValue;
    private String codeDesc;

    private CaptchaTypeEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    public String getCodeValue() {
        return this.codeValue;
    }

    public String getCodeDesc() {
        return this.codeDesc;
    }

    public static CaptchaTypeEnum parseFromCodeValue(String codeValue) {
        CaptchaTypeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CaptchaTypeEnum e = var1[var3];
            if (e.codeValue.equals(codeValue)) {
                return e;
            }
        }

        return null;
    }

    public static String getCodeDescByCodeBalue(String codeValue) {
        CaptchaTypeEnum enumItem = parseFromCodeValue(codeValue);
        return enumItem == null ? "" : enumItem.getCodeDesc();
    }

    public static boolean validateCodeValue(String codeValue) {
        return parseFromCodeValue(codeValue) != null;
    }

    public static String getString() {
        StringBuffer buffer = new StringBuffer();
        CaptchaTypeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CaptchaTypeEnum e = var1[var3];
            buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
        }

        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }
}
