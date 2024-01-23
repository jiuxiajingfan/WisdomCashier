package com.li.wisdomcashier.strategy.email;

import com.li.wisdomcashier.controller.email.dto.EmailDTO;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.enums.EmailEnums;
import com.li.wisdomcashier.utils.UserUtils;
import org.springframework.stereotype.Component;

import static com.li.wisdomcashier.constant.MQConstant.ROUTING_EXCHANGE_EMAIL;
import static com.li.wisdomcashier.constant.MQConstant.ROUTING_KEY_REGISTER;

/**
 * @ClassName ChangeEmailStrategy
 * @Description TODO
 * @Author Nine
 * @Date 2023/11/6 11:56
 * @Version 1.0
 */
@Component
public class ChangeEmailStrategy extends AbstractEmailStrategy {
    @Override
    protected EmailEnums getTypeEnum() {
        return EmailEnums.CHANGE_EMAIL;
    }

    @Override
    public R<String> send(String email) {
        email = UserUtils.getUser().getEmail();
        rabbitTemplate.convertAndSend(ROUTING_EXCHANGE_EMAIL, ROUTING_KEY_REGISTER,
                EmailDTO.builder()
                        .email(email)
                        .type(getTypeEnum().getValue())
                        .desc(getTypeEnum().getDesc())
                        .build()
        );
        return R.ok("发送成功，请耐心等待~");
    }
}
