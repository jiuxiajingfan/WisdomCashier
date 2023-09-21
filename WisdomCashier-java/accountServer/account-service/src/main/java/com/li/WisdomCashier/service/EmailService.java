package com.li.WisdomCashier.service;

import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.strategy.email.AbstractEmailStrategy;
import com.li.WisdomCashier.strategy.email.EmailStrategyFactory;
import com.li.WisdomCashier.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName EmailService
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/12 17:22
 * @Version 1.0
 */
@Service
public class EmailService {

    @Resource
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Async("taskExecutor")
    public void sendSimpleMail(String to,String code,String title) {
        SimpleMailMessage message = EmailUtils.simple(mailFrom, to,code,title);
        mailSender.send(message);
    }

    @Async("taskExecutor")
    public void sendMultiPartMail(String from, String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessage mimeMessage = EmailUtils.mimeMessage(message,mailFrom, to);
        mailSender.send(mimeMessage);

    }

    /**
     * 发送邮件
     * @param email 邮箱
     * @param type 邮件类型 {@link = com.li.WisdomCashier.enums.EmailEnums}
     * @return
     */
    public R<String> getCode(String email,Integer type){
        AbstractEmailStrategy emailStrategy = EmailStrategyFactory.getEmailStrategy(type);
        return emailStrategy.Send(email);
    }


}
