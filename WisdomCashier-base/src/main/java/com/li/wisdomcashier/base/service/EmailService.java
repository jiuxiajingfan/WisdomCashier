package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.entity.po.R;
import com.li.wisdomcashier.base.util.CodeUtils;
import com.li.wisdomcashier.base.util.EmailUtils;
import com.li.wisdomcashier.base.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Resource
    private RedisUtils redisUtils;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Async("threadPool")
    public void sendSimpleMail(String from, String to,String code) {
        SimpleMailMessage message = EmailUtils.simple(from, to,code);
        try {
            Thread.sleep(4000);
            mailSender.send(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Async("threadPool")
    public void sendMultiPartMail(String from, String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessage mimeMessage = EmailUtils.mimeMessage(message,from, to);
        mailSender.send(mimeMessage);

    }

    public R<String> getCode(String email) {
        if (redisUtils.hasKey(email)) {
            return R.error("请勿重复调用~");
        } else {
            String code = CodeUtils.getCode();
            redisUtils.set(email, code,90);
            this.sendSimpleMail(mailFrom, email, code);
        }
        return R.ok();
    }
}
