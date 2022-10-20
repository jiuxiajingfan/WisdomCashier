package com.li.wisdomcashier.base.common;

/**
 * @ClassName StatusFailException
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/20 22:07
 * @Version 1.0
 */
public class StatusFailException extends Exception{
    public StatusFailException() {
    }

    public StatusFailException(String message) {
        super(message);
    }

    public StatusFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusFailException(Throwable cause) {
        super(cause);
    }

    public StatusFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}