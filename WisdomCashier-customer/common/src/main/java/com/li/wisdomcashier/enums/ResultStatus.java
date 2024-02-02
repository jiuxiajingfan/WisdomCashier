package com.li.wisdomcashier.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ResultStatus
 * @Description HTTP回复
 * @Author Nine
 * @Date 2022/10/20 22:00
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ResultStatus {

    SUCCESS(200, "成功"),

    FAIL(400, "失败"),

    ACCESS_DENIED(403, "访问受限,您权限不足"),

    FORBIDDEN(401, "请先登录"),

    NOT_FOUND(404, "数据不存在"),

    SYSTEM_ERROR(500, "系统错误"),

    CURRENT_LIMIT(400,"活动太火爆了，再试试吧"),

    PARAM_MISSING(400,"缺失必要参数：");


    private final int status;

    private final String description;
}
