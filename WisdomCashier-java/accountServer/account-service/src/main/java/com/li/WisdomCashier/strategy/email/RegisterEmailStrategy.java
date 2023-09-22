package com.li.WisdomCashier.strategy.email;

import com.li.WisdomCashier.dto.EmailDTO;
import com.li.WisdomCashier.enums.EmailEnums;
import com.li.WisdomCashier.pojo.R;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.li.WisdomCashier.constant.MQConstant.ROUTING_EXCHANGE_EMAIL;
import static com.li.WisdomCashier.constant.MQConstant.ROUTING_KEY_REGISTER;

/**
 * 注册邮件策略
 */
@Component
public class RegisterEmailStrategy extends AbstractEmailStrategy{
    @Override
    protected EmailEnums getTypeEnum() {
        return EmailEnums.REGISTER;
    }

    @Override
    public R<String> Send(EmailDTO email) {
        if(Objects.isNull(email.getEmail()))
            return R.error("请输入邮箱！");
        if (redisUtils.hasKey(this.getTypeEnum().getValue() + email.getEmail()))
            return R.error("发送频繁，请耐心等待！");
        rabbitTemplate.convertAndSend(ROUTING_EXCHANGE_EMAIL,ROUTING_KEY_REGISTER,email);
        return R.ok("发送成功，请耐心等待~");
    }
}
