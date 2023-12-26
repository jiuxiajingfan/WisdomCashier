package com.li.widomcashier.config;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.exception.BusinessException;
import com.li.wisdomcashier.exception.CommonErrorEnum;
import com.li.wisdomcashier.exception.FrequencyControlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * validation参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(x.getDefaultMessage()).append(","));
        String message = errorMsg.toString();
        log.warn("validation parameters error！The reason is:{}", message);
        return R.error(message.substring(0, message.length() - 1), CommonErrorEnum.PARAM_VALID.getErrorCode());
    }

    /**
     * validation参数校验异常
     */
    @ExceptionHandler(value = BindException.class)
    public R bindException(BindException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(x.getDefaultMessage()).append(","));
        String message = errorMsg.toString();
        log.warn("validation parameters error！The reason is:{}", message);
        return R.error(message.substring(0, message.length() - 1),CommonErrorEnum.PARAM_VALID.getErrorCode());
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public R exceptionHandler(NullPointerException e) {
        log.warn("null point exception！The reason is: ", e);
        return R.error("系统异常");
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public R systemExceptionHandler(Exception e) {
        log.warn("system exception！The reason is：{}", e.getMessage(), e);
        return R.error("系统异常");
    }

    /**
     * 自定义校验异常（如参数校验等）
     */
    @ExceptionHandler(value = BusinessException.class)
    public R businessExceptionHandler(BusinessException e) {
        log.warn("business exception！The reason is：{}", e.getMessage(), e);
        return R.error(e.getMessage(),e.getErrorCode());
    }

    /**
     * http请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handleException(HttpRequestMethodNotSupportedException e) {
        log.info(e.getMessage(), e);
        return R.error(String.format("不支持'%s'请求", e.getMethod()));
    }

    /**
     * 限流异常
     */
    @ExceptionHandler(value = FrequencyControlException.class)
    public R frequencyControlExceptionHandler(FrequencyControlException e) {
        log.warn("frequencyControl exception！The reason is：{}", e.getMessage(), e);
        return R.error(e.getMessage(),e.getErrorCode());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e){
        return R.error(CommonErrorEnum.LACK_PARAM.getMsg(),CommonErrorEnum.LACK_PARAM.getErrorCode());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public R accessDeniedException(AccessDeniedException e){
        return R.error(CommonErrorEnum.ACCESS_LACK.getMsg(),CommonErrorEnum.ACCESS_LACK.getErrorCode());
    }
}
