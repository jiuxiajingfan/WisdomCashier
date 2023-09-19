package com.li.WisdomCashier.error;

import com.li.WisdomCashier.pojo.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @ClassName MssWebResponseExceptionTranslator
 * @Description 自定义认证异常翻译器
 * @Author Nine
 * @Date 2023/7/23 12:34
 * @Version 1.0
 */
public class MssWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<R> translate(Exception e) throws Exception {

        R translator = Translator(e);
        // 方便feign调用返回200
        return new ResponseEntity<>(translator, HttpStatus.OK);
    }

    private R Translator(Exception e){
        if (e instanceof InvalidGrantException) {
            return R.error("账号或密码错误！",401);
        }else if(e instanceof InvalidTokenException){
            return R.error("账号已失效，请重新登录！");
        }
        return R.error("验证失败！",401);
    }
}
