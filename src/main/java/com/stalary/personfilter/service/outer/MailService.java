package com.stalary.personfilter.service.outer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * MailService
 *
 * @author lirongqian
 * @since 2018/04/27
 */
@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.address}")
    private String sender;

    /**
     * 发送不带附件的简单邮件，用于提示账号登录异常
     * 邮件由于不存在id，所以默认id设置为-1
     *
     * @param receiver 收件人
     */
    @SneakyThrows
    public void sendSimpleMail(String receiver, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(sender);
        message.setSubject("收到投递简历");
        message.setText(content);
        // 抄送
        message.setCc(receiver);
        log.info("进入邮件");
        mailSender.send(message);
        log.info("向" + receiver + "发送邮件成功");
    }
}
