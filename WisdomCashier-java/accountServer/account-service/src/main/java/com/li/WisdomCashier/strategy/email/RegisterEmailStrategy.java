package com.li.WisdomCashier.strategy.email;

import com.li.WisdomCashier.enums.EmailEnums;

/**
 * 注册邮件策略
 */
public class RegisterEmailStrategy extends AbstractEmailStrategy{
    @Override
    protected EmailEnums getTypeEnum() {
        return EmailEnums.REGISTER;
    }

    @Override
    protected String Send() {
        return null;
    }
}
