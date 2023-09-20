package com.li.WisdomCashier.strategy.email;

import com.li.WisdomCashier.enums.EmailEnums;

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
    /**
     * 获取自身枚举用于映射
     * @return
     */
    protected abstract EmailEnums getTypeEnum();

    /**
     * 发送邮件
     * @return
     */
    protected abstract String Send();

    /**
     * 向工厂注册自身
     */
    @PostConstruct
    private void init(){
        EmailStrategyFactory.register(getTypeEnum().getType(),this);
    };
}
