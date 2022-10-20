package com.li.wisdomcashier.base.common;

/**
 * @ClassName AccessException
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/20 22:23
 * @Version 1.0
 */
public class AccessException extends Exception{
    public AccessException() {
        super();
    }

    public AccessException(String message) {
        super(message);
    }

    public AccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessException(Throwable cause) {
        super(cause);
    }

    protected AccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}