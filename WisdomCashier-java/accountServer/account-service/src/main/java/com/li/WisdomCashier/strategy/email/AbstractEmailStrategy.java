package com.li.WisdomCashier.strategy.email;

import com.li.WisdomCashier.dto.EmailDTO;
import com.li.WisdomCashier.enums.EmailEnums;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.utils.RedisUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
     * @return
     */
    protected abstract EmailEnums getTypeEnum();

    /**
     * 发送邮件
     * @return
     */
    public abstract R<String> Send(EmailDTO email);

    /**
     * 向工厂注册自身
     */
    @PostConstruct
    private void init(){
        EmailStrategyFactory.register(getTypeEnum().getType(),this);
    };
}
