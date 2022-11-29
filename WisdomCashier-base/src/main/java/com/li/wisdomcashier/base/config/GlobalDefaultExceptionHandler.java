package com.li.wisdomcashier.base.config;

import com.google.protobuf.ServiceException;
import com.li.wisdomcashier.base.common.*;
import com.li.wisdomcashier.base.enums.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Set;
import com.li.wisdomcashier.base.common.AccessException;

/**
 * @ClassName GlobalDefaultExceptionHandler
 * @Description 全局异常捕捉
 * @Author Nine
 * @Date 2022/10/13 13:39
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {


    /**
     * 400 - Internal Server Error 自定义通用异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {StatusForbiddenException.class,
            StatusAccessDeniedException.class,
            StatusFailException.class,
            StatusNotFoundException.class,
            StatusSystemErrorException.class})
    public R<Void> handleCustomException(Exception e) {
        return R.error(e.getMessage(), ResultStatus.FAIL);
    }


    /**
     * 401 -UnAuthorized 处理AuthenticationException,token相关异常 即是认证出错 可能无法处理！
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = AuthenticationException.class)
    public R<Void> handleAuthenticationException(AuthenticationException e,
                                                            HttpServletRequest httpRequest,
                                                            HttpServletResponse httpResponse) {
        httpResponse.setHeader("Url-Type", httpRequest.getHeader("Url-Type")); // 为了前端能区别请求来源
        return R.error(e.getMessage(), ResultStatus.ACCESS_DENIED);
    }

    /**
     * 401 -UnAuthorized UnauthenticatedException,token相关异常 即是认证出错 可能无法处理！
     * 没有登录（没有token），访问有@RequiresAuthentication的请求路径会报这个异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnauthenticatedException.class)
    public R<Void> handleUnauthenticatedException(UnauthenticatedException e,
                                                             HttpServletRequest httpRequest,
                                                             HttpServletResponse httpResponse) {
        httpResponse.setHeader("Url-Type", httpRequest.getHeader("Url-Type")); // 为了前端能区别请求来源
        return R.error("未登录或是登录有效期已过期，请登录！", ResultStatus.ACCESS_DENIED);
    }

    /**
     * 403 -FORBIDDEN AuthorizationException异常 即是授权认证出错 可能无法处理！
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AuthorizationException.class)
    public R<Void> handleAuthenticationException(AuthorizationException e,
                                                            HttpServletRequest httpRequest,
                                                            HttpServletResponse httpResponse) {
        httpResponse.setHeader("Url-Type", httpRequest.getHeader("Url-Type")); // 为了前端能区别请求来源
        return R.error("对不起，您无权限进行此操作！", ResultStatus.FORBIDDEN);
    }

    /**
     * 403 -FORBIDDEN 处理shiro的异常 无法处理！ 未能走到controller层
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = ShiroException.class)
    public R<Void> handleShiroException(ShiroException e,
                                        @NotNull HttpServletRequest httpRequest,
                                        HttpServletResponse httpResponse) {
        httpResponse.setHeader("Url-Type", httpRequest.getHeader("Url-Type")); // 为了前端能区别请求来源
        return R.error("对不起，您无权限进行此操作，请先登录进行授权认证", ResultStatus.FORBIDDEN);
    }

    /**
     * 403 -FORBIDDEN 处理访问api能力被禁止的异常
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessException.class)
    public R<Void> handleAccessException(AccessException e) {
        return R.error(e.getMessage(), ResultStatus.FORBIDDEN);
    }

    /**
     * 400 - Bad Request 处理Assert的异常 断言的异常！
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public R<Void> handler(IllegalArgumentException e) {
        return R.error(e.getMessage(), ResultStatus.FAIL);
    }

    /**
     * 400 - Bad Request @Validated 校验错误异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<Void> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) throws IOException {
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return R.error(objectError.getDefaultMessage(), ResultStatus.FAIL);
    }


    /**
     * 400 - Bad Request 处理缺少请求参数
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<Void> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        return R.error("缺少请求参数：" + e.getMessage(), ResultStatus.FAIL);
    }

    /**
     * 400 - Bad Request 参数解析失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        return R.error("参数解析失败", ResultStatus.FAIL);
    }


    /**
     * 400 - Bad Request 参数绑定失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return R.error(message, ResultStatus.FAIL);
    }

    /**
     * 400 - Bad Request 参数验证失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleServiceException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return R.error("[参数验证失败]parameter:" + message, ResultStatus.FAIL);
    }

    /**
     * 400 - Bad Request 实体校验失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public R<Void> handleValidationException(ValidationException e) {
        return R.error("参数错误！", ResultStatus.FAIL);
    }

    /**
     * 405 - Method Not Allowed 不支持当前请求方法
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return R.error("不支持当前请求方法", ResultStatus.FAIL);
    }

    /**
     * 415 - Unsupported Media Type 不支持当前媒体类型
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R<Void> handleHttpMediaTypeNotSupportedException(Exception e) {
        return R.error("不支持当前媒体类型", ResultStatus.FAIL);
    }


    /**
     * 500 - Internal Server Error 处理邮件发送出现的异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = MessagingException.class)
    public R<Void> handler(MessagingException e) {
        log.error("邮箱系统异常-------------->{}", getMessage(e));
        return R.error("服务器错误，请稍后重试", ResultStatus.SYSTEM_ERROR);
    }

    /**
     * 500 - Internal Server Error 业务逻辑异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        log.error("业务逻辑异常-------------->{}", getMessage(e));
        return R.error("服务器错误，请稍后重试", ResultStatus.SYSTEM_ERROR);
    }

    /**
     * 500 - Internal Server Error 操作数据库出现异常:名称重复，外键关联
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public R<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("操作数据库出现异常-------------->{}", getMessage(e));
        return R.error("服务器错误，请稍后重试", ResultStatus.SYSTEM_ERROR);
    }


    /**
     * 500 - Internal Server Error 操作数据库出现异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public R<Void> handleSQLException(SQLException e) {
        log.error("操作数据库出现异常-------------->{}", getMessage(e));
        return R.error("服务器错误，请稍后重试", ResultStatus.SYSTEM_ERROR);
    }

    /**
     * 500 - Internal Server Error 批量操作数据库出现异常:名称重复，外键关联
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PersistenceException.class)
    public R<Void> handleBatchUpdateException(PersistenceException e) {
        log.error("操作数据库出现异常-------------->{}", getMessage(e));
        return R.error("请检查数据是否准确！可能原因：数据库中已有相同的数据导致重复冲突!", ResultStatus.SYSTEM_ERROR);
    }


    /**
     * 500 - Internal Server Error 系统通用异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统通用异常-------------->{}", getMessage(e));
        return R.error("服务器错误，请稍后重试", ResultStatus.SYSTEM_ERROR);
    }


    /**
     * 打印异常信息
     */
    public static String getMessage(Exception e) {
        String swStr = null;
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            swStr = sw.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
        return swStr;
    }
}
