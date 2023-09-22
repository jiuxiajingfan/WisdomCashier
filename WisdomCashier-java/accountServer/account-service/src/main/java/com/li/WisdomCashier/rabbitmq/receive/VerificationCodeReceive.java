package com.li.WisdomCashier.rabbitmq.receive;

import cn.hutool.core.util.RandomUtil;
import com.li.WisdomCashier.dto.EmailDTO;
import com.li.WisdomCashier.service.EmailService;
import com.li.WisdomCashier.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.li.WisdomCashier.constant.MQConstant.*;


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
            key = ROUTING_KEY_REGISTER
    )
    )
    public void sendCode(EmailDTO emailDTO) {
        String code = RandomUtil.randomString(6);
        emailService.sendSimpleMail(emailDTO.getEmail(), code, "注册验证码");
        redisUtils.set( emailDTO.getType() + emailDTO.getEmail(),code,300);
    }
}
