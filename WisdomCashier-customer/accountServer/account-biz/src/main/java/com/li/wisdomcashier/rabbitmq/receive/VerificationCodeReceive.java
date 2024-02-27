package com.li.wisdomcashier.rabbitmq.receive;

import cn.hutool.core.util.RandomUtil;
import com.li.wisdomcashier.controller.email.dto.EmailDTO;
import com.li.wisdomcashier.service.EmailService;
import com.li.wisdomcashier.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.li.wisdomcashier.constant.MQConstant.*;


/**
 * @ClassName VerificationCodeReceive
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/26 16:35
 * @Version 1.0
 */
@Component
@Slf4j
public class VerificationCodeReceive {


    @Resource
    RedisUtils redisUtils;

    @Resource
    EmailService emailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ROUTING_MSG_EMAIL),
            exchange = @Exchange(name = ROUTING_EXCHANGE_EMAIL),
            key = ROUTING_KEY_EMAIL
    )
    )
    public void sendCode(EmailDTO emailDTO) {
        String code = RandomUtil.randomString(6);
        emailService.sendSimpleMail(emailDTO.getEmail(), code, emailDTO.getDesc());
        redisUtils.set(emailDTO.getType() + emailDTO.getEmail(), code, 300);
    }

}
