package com.li.wisdomcashier.strategy.email;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.enums.EmailEnums;
import com.li.wisdomcashier.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @ClassName AbstractEmailStrategy
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/20 17:08
 * @Version 1.0
 */
public abstract class AbstractEmailStrategy {
    @Resource
    protected RabbitTemplate rabbitTemplate;

    @Resource
    protected RedisUtils redisUtils;

    /**
     * 获取自身枚举用于映射
     *
     * @return
     */
    protected abstract EmailEnums getTypeEnum();

    /**
     * 发送邮件
     *
     * @return
     */
    public abstract R<String> send(String email);

    /**
     * 向工厂注册自身
     */
    @PostConstruct
    private void init() {
        EmailStrategyFactory.register(getTypeEnum().getType(), this);
    }
}
