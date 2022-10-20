package com.li.wisdomcashier.base.common;

/**
 * @ClassName StatusSystemErrorException
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/20 22:08
 * @Version 1.0
 */
public class StatusSystemErrorException extends Exception {

    public StatusSystemErrorException() {
    }

    public StatusSystemErrorException(String message) {
        super(message);
    }

    public StatusSystemErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusSystemErrorException(Throwable cause) {
        super(cause);
    }

    public StatusSystemErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}