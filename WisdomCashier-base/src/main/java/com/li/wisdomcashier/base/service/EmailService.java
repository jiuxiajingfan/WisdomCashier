package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.util.EmailUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
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

    @Async("threadPool")
    public void sendSimpleMail(String to,String code) {
        SimpleMailMessage message = EmailUtils.simple(mailFrom, to,code);
        mailSender.send(message);
    }

    @Async("threadPool")
    public void sendMultiPartMail(String from, String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessage mimeMessage = EmailUtils.mimeMessage(message,mailFrom, to);
        mailSender.send(mimeMessage);

    }


}
