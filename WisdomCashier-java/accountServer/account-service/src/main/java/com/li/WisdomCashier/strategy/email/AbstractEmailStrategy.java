package com.li.WisdomCashier.strategy.email;

import com.li.WisdomCashier.enums.EmailEnums;

import javax.annotation.PostConstruct;

/**
 * @ClassName AbstractEmailStrategy
 * @Description TODO
 * @Author Nine
 * @Date 2023/9/20 17:08
 * @Version 1.0
 */
public abstract class AbstractEmailStrategy {

    protected abstract EmailEnums getTypeEnum();

    /**
     * 向工厂注册
     */
    @PostConstruct
    private void init(){
        EmailStrategyFactory.register(getTypeEnum());
    };


}
