package com.li.wisdomcashier.entry;

import com.li.wisdomcashier.enums.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName R
 * @Description 统一消息处理
 * @Author Nine
 * @Date 2022/10/11 16:34
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> implements Serializable {
    private Integer code;   // 状态码

    private T data;   // 返回的数据

    private String msg;    // 自定义信息

    /**
     * 成功的结果
     *
     * @param data 返回结果
     * @param msg  返回信息
     */
    public static <T> R<T> ok(T data, String msg) {
        return new R<>(ResultStatus.SUCCESS.getStatus(), data, msg);
    }


    /**
     * 成功的结果
     *
     * @param data 返回结果
     */
    public static <T> R<T> ok(T data) {
        return new R<T>(ResultStatus.SUCCESS.getStatus(), data, "success");
    }

    /**
     * 成功的结果
     *
     * @param msg 返回信息
     */
    public static <T> R<T> ok(String msg) {
        return new R<T>(ResultStatus.SUCCESS.getStatus(), null, msg);
    }

    /**
     * 成功的结果
     */
    public static <T> R<T> ok() {
        return new R<T>(ResultStatus.SUCCESS.getStatus(), null, "success");
    }


    /**
     * 失败的结果，无异常
     *
     * @param msg 返回信息
     */
    public static <T> R<T> error(String msg) {
        return new R<T>(ResultStatus.FAIL.getStatus(), null, msg);
    }

    public static <T> R<T> error(ResultStatus resultStatus) {
        return new R<T>(resultStatus.getStatus(), null, resultStatus.getDescription());
    }

    public static <T> R<T> error(String msg, ResultStatus resultStatus) {
        return new R<T>(resultStatus.getStatus(), null, msg);
    }

    public static <T> R<T> error(String msg, Integer status) {
        return new R<T>(status, null, msg);
    }

}