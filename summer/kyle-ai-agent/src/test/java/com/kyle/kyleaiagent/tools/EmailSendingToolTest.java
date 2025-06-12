package com.kyle.kyleaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendingToolTest {
    @Value("${email.from-email}")
    private String fromEmail;

    @Value("${email.auth-password}")
    private String password;

    @Test
    void sendEmail() {
        EmailSendingTool emailSendingTool = new EmailSendingTool(fromEmail, password);
        String result = emailSendingTool.sendEmail("xxxx@163.com", "测试", "testIf");
        Assertions.assertNotNull(result);
    }
}