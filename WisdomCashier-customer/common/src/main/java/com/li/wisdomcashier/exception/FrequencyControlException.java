package com.li.wisdomcashier.exception;

import lombok.Data;

/**
 * 自定义限流异常
 *
 * @date 2023/07/034
 */
@Data
public class FrequencyControlException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected Integer errorCode;

    /**
     * 错误信息
     */
    protected String errorMsg;

    public FrequencyControlException() {
        super();
    }

    public FrequencyControlException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public FrequencyControlException(ErrorEnum error) {
        super(error.getErrorMsg());
        this.errorCode = error.getErrorCode();
        this.errorMsg = error.getErrorMsg();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
