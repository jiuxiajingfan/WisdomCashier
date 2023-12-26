package com.li.wisdomcashier.strategy.email;

import com.li.wisdomcashier.dto.EmailDTO;
import com.li.wisdomcashier.enums.EmailEnums;
import com.li.wisdomcashier.entry.R;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.li.wisdomcashier.constant.MQConstant.ROUTING_EXCHANGE_EMAIL;
import static com.li.wisdomcashier.constant.MQConstant.ROUTING_KEY_REGISTER;

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
    public R<String> Send(String email) {
        if(Objects.isNull(email))
            return R.error("请输入邮箱！");
        if (redisUtils.hasKey(this.getTypeEnum().getValue() + email))
            return R.error("发送频繁，请耐心等待！");
        rabbitTemplate.convertAndSend(ROUTING_EXCHANGE_EMAIL,ROUTING_KEY_REGISTER,
                EmailDTO.builder()
                        .email(email)
                        .type(getTypeEnum().getValue())
                        .desc(getTypeEnum().getDesc())
                        .build()
        );
        return R.ok("发送成功，请耐心等待~");
    }
}
