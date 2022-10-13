package com.li.wisdomcashier.base.config;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @ClassName GlobalDefaultExceptionHandler
 * @Description 全局异常捕捉
 * @Author Nine
 * @Date 2022/10/13 13:39
 * @Version 1.0
 */
@ControllerAdvice(basePackages = {"com.li.wisdomcashier"})
@Slf4j
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(value = RequestException.class)
    @ResponseBody
    public R<String> requestExceptionHandler(RequestException e) {
        if (e.getE() != null) {
            log.error("",e);
        }
        return R.error(e.getMsg());
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    public R<String> requestExceptionHandler(DataIntegrityViolationException e) {
        log.error("数据操作格式异常", e);
        return R.error("数据操作格式异常，" + e.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String s = "参数验证失败";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            s = errors.get(0).getDefaultMessage();
        }
        return R.error(s);
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public R<String> bindExceptionHandler(BindException e) {
        log.error("bindExceptionHandler", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        if (allErrors.isEmpty()) {
            return R.error("服务器飘了，管理员去拿刀修理了~");
        } else {
            return R.error(allErrors.get(0).getDefaultMessage());
        }
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public R<String> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("ConstraintViolationExceptionHandler", e);
        if(e.getConstraintViolations().iterator().hasNext()){
            String error=e.getConstraintViolations().iterator().next().getMessageTemplate();
            return R.error(error==null?"参数校验错误":error);
        }
        return R.error("参数校验错误");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R<String> requestExceptionHandler(Exception e) {
        log.error("",e);
        return R.error("服务器飘了，管理员去拿刀修理了~");
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public R<String> requestHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("",e);
        return R.error("请求参数格式错误,不符合正常的请求参数格式");
    }

}
