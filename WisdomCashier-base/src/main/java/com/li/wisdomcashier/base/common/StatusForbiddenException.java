package com.li.wisdomcashier.base.common;

/**
 * @ClassName StatusForbiddenException
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/20 22:05
 * @Version 1.0
 */
public class StatusForbiddenException extends Exception{

    public StatusForbiddenException() {
    }

    public StatusForbiddenException(String message) {
        super(message);
    }

    public StatusForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusForbiddenException(Throwable cause) {
        super(cause);
    }

    public StatusForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}