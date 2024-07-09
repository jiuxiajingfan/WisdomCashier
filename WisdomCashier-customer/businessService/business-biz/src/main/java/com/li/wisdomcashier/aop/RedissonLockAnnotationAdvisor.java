package com.li.wisdomcashier.aop;

import com.li.wisdomcashier.annotation.RedissonLock;
import com.li.wisdomcashier.exception.BusinessException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class RedissonLockAnnotationAdvisor {

    @Resource
    private RedissonClient redissonClient;

    private final ExpressionParser parser = new SpelExpressionParser();

    private DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) {
        Method method = this.getMethod(joinPoint);
        Object[] arguments = joinPoint.getArgs();
        Optional<String> key = Optional.of(parseSpel(method, arguments, redissonLock.key(), String.class, redissonLock.key()));
        RLock lock = redissonClient.getLock(redissonLock.keyPrefix() + key.get());
        try {
            log.info("{}尝试上锁", Thread.currentThread().getName() +redissonLock.keyPrefix() + key.get());
            boolean flag = lock.tryLock(redissonLock.time(),redissonLock.expire() ,redissonLock.unit());
            if (flag) {
                log.info("{}上锁成功", Thread.currentThread().getName() +redissonLock.keyPrefix() + key.get());
                return joinPoint.proceed();
            } else {
                log.info("{}操作频繁，请稍后再试~", redissonLock.keyPrefix() + key.get());
                throw new BusinessException("操作频繁，请稍后再试~");
            }
        } catch (Throwable e) {
            throw new BusinessException("操作频繁，请稍后再试~");
        } finally {
            //锁状态
            if (lock.isLocked()) {
                //当前线程
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    log.info("{}已解锁", redissonLock.keyPrefix() + key.get());
                }
            }
        }
    }

    /**
     * 解析 spel 表达式
     *
     * @param method        方法
     * @param arguments     参数
     * @param spel          表达式
     * @param clazz         返回结果的类型
     * @param defaultResult 默认结果
     * @return 执行spel表达式后的结果
     */
    private <T> T parseSpel(Method method, Object[] arguments, String spel, Class<T> clazz, T defaultResult) {
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < Objects.requireNonNull(params).length; len++) {
            context.setVariable(params[len], arguments[len]);
        }
        try {
            Expression expression = parser.parseExpression(spel);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            return defaultResult;
        }
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return method;
    }
}
