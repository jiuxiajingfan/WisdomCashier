package com.li.wisdomcashier.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 通用异常码
 * Date: 2023-03-26
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum {

    SYSTEM_ERROR(500, "系统出小差了，请稍后再试哦~~"),
    PARAM_VALID(400, "参数校验失败{0}"),
    FREQUENCY_LIMIT(-3, "请求太频繁了，请稍后再试哦~~"),
    LOCK_LIMIT(-4, "请求太频繁了，请稍后再试哦~~"),
    LACK_PARAM(400,"缺少参数{0}"),
    ACCESS_LACK(403,"缺少访问权限")
    ;
    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
