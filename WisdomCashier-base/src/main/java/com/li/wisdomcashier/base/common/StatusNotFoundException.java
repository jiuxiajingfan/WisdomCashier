package com.li.wisdomcashier.base.common;

/**
 * @ClassName StatusNotFoundException
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/20 22:07
 * @Version 1.0
 */
public class StatusNotFoundException extends Exception{

    public StatusNotFoundException() {
    }

    public StatusNotFoundException(String message) {
        super(message);
    }

    public StatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusNotFoundException(Throwable cause) {
        super(cause);
    }

    public StatusNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}