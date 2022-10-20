package com.li.wisdomcashier.base.entity.po;


import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName JWTFilter
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/19 23:17
 * @Version 1.0
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 过滤器拦截请求的入口方法
     * 是否允许访问，如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("检查是否携带Token");
        if (isLoginAttempt(request, response)) {//请求头包含Token
            try { //如果存在，则进入 executeLogin 方法，检查 token 是否正确
                log.info("执行===》executeLogin");
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                //token 错误
                responseError(response, e.getMessage());
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    /**
     * 判断用户是否已经登录
     * 检测 header 里面是否包含 Token 字段
     */

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        return token != null;
    }

    /**
     * Shiro认证操作
     * executeLogin实际上就是先调用createToken来获取token，这里我们重写了这个方法，就不会自动去调用createToken来获取token
     * 然后调用getSubject方法来获取当前用户再调用login方法来实现登录
     * 这也解释了我们为什么要自定义jwtToken，因为我们不再使用Shiro默认的UsernamePasswordToken了。
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");

        JWTToken jwt = new JWTToken(token);
        //交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        //log.info("获取的Token为" + ((HttpServletRequest) request).getHeader("Authorization"));
        try {
            log.info("进入Shiro认证");
            getSubject(request, response).login(jwt);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 在JwtFilter处理逻辑之前，进行跨域处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("进入预处理器--处理完成进入JwtFilter");
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res= (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin",req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers",req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 非法请求
     */
    private void responseError(ServletResponse response, String message) {
        try {
            log.info("非法请求"+message);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

}
