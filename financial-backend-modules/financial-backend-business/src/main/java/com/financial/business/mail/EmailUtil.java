package com.financial.business.mail;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 邮件服务
 * @author xinyi
 */
@Service
public class EmailUtil {

  @Resource
  private JavaMailSender mailSender;

  @Value("${spring.mail.from:levent_guan@163.com}")
  private String from;

  /**
   * 简单发送
   */
  private void basicSend(String to, String subject, String content){
    SimpleMailMessage message = new SimpleMailMessage();
    //邮件发送人
    message.setFrom(from);
    //邮件接收人
    message.setTo(to);
    //邮件主题
    message.setSubject(subject);
    //邮件内容
    message.setText(content);
    mailSender.send(message);
  }

  /**
   * 发送html邮件
   */
  public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    //base64解码
    content = new String(java.util.Base64.getDecoder().decode(content));
    helper.setText(content, true);
    mailSender.send(mimeMessage);
  }

  /**
   * 发送带附件的邮件
   */
  public void sendAttachmentMail(String to, String subject, String content) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText("<html><body><span style='color:red'>你好，我是小红</span></body></html>", true);
    helper.addAttachment("附件-1.txt", new java.io.File("D:\\financial\\Financial\\financial-backend-modules\\financial-backend-business\\src\\main\\resources\\Snipaste_2025-04-27_12-19-12.png"));
    mailSender.send(mimeMessage);
  }
}
