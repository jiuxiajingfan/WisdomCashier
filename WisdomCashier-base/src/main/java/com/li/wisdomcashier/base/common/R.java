package com.li.wisdomcashier.base.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName R
 * @Description 统一消息处理
 * @Author Nine
 * @Date 2022/10/11 16:34
 * @Version 1.0
 */
@Data
@Slf4j
public class R<T> implements Serializable {
    public static final String SUCCESS_MSG = "success";
    private static final long serialVersionUID = 1L;
    public static final int FAIL_CODE = 1;
    public static final int SUCCESS_CODE = 0;

    @Getter
    @Setter
    private int code = 0;

    @Getter
    @Setter
    private String msg = SUCCESS_MSG;


    @Getter
    @Setter
    private T data;


    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setData(null);
        r.setCode(SUCCESS_CODE);
        r.setMsg(SUCCESS_MSG);
        return r;
    }


    public R(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = FAIL_CODE;
    }

    public static <T> R<T> errorAndLog(String msg) {
        R<T> r = new R<>();
        r.setMsg(msg);
        r.setCode(FAIL_CODE);
        log.error(msg);
        return r;
    }

    public static <T> R<T> errorAndLog(R<?> rr) {
        R<T> r = new R<>();
        r.setMsg(rr.getMsg());
        r.setCode(FAIL_CODE);
        return r;
    }


    public static <T> R<T> error(R<?> rr) {
        R<T> r = new R<>();
        r.setMsg(r.getMsg());
        r.setMsg(rr.getMsg());
        r.setCode(FAIL_CODE);
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.setMsg(msg);
        r.setCode(FAIL_CODE);
        return r;
    }

    public static <T> R<T> success(String msg, T data) {
        R<T> r = new R<>();
        r.setMsg(msg);
        r.setCode(SUCCESS_CODE);
        r.setData(data);
        return r;
    }

    public static <T> R<T> successMsg(String msg) {
        R<T> r = new R<>();
        r.setCode(SUCCESS_CODE);
        r.setMsg(msg);
        return r;
    }

    public R(T data, String msg, int code) {
        super();
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setData(data);
        return r;
    }

    public static <T> R<T> op(int count) {
        return count > 0 ? R.successMsg("操作成功"):R.error("操作失败");
    }



    public boolean hasError() {
        return this.code != SUCCESS_CODE;
    }

}