package com.li.wisdomcashier.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * @ClassName EmailUtils
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/12 17:06
 * @Version 1.0
 */
public class EmailUtils {
    private static final String content = "这是邮件的主内容";

    // 纯文本邮件
    public static SimpleMailMessage simple(String from, String to, String code, String title) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText("您的验证码为：" + code + ",请在5分钟内使用");
        return message;
    }

    // 多媒体邮件
    public static MimeMessage mimeMessage(MimeMessage message, String from, String to) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("复杂邮件主题");
        helper.setText("<p style='color: deepskyblue'>这是浅蓝色的文字</p>", true);
//        helper.addAttachment("1.docx", new File("C:\\Users\\Splay\\Desktop\\说明书.docx"));
//        helper.addAttachment("1.pdf", new File("C:\\Users\\Splay\\Desktop\\参考.pdf"));
        return message;
    }
}
