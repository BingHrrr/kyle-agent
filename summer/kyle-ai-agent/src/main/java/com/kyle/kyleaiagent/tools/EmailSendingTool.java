package com.kyle.kyleaiagent.tools;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Properties;

public class EmailSendingTool {
    private final String fromEmail;

    private final String password;

    public EmailSendingTool(String fromEmail, String password) {
        this.fromEmail = fromEmail;
        this.password = password;
    }

    @Tool(description = "Send an email to a specified recipient")
    public String sendEmail(
            @ToolParam(description = "Recipient's email address") String to,
            @ToolParam(description = "Email subject") String subject,
            @ToolParam(description = "Email body text") String body
    ) {

        // 设置邮件服务器属性（以SMTP为例）
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.163.com"); // 修改为你的SMTP服务器地址
        props.put("mail.smtp.port", "25");


        // 创建 Session 对象
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // 创建邮件对象
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(body);

            // 发送邮件
            Transport.send(message);
            return "Email sent successfully to " + to;

        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
