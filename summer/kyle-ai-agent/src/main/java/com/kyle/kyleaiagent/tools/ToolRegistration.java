package com.kyle.kyleaiagent.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Haoran Wang
 * @date 2025/6/12 13:41
 */
@Configuration
public class ToolRegistration {
    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Value("${email.from-email}")
    private String fromEmail;

    @Value("${email.auth-password}")
    private String password;
    /**
     * 使用
     * 注册器模式（集中注册和管理Tool）
     * 适配器模式（ToolCallbacks.from）
     * @return ToolCallback[]
     */
    @Bean
    public ToolCallback[] allTools() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        EmailSendingTool emailSendingTool = new EmailSendingTool(fromEmail, password);
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
                webScrapingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGenerationTool,
                emailSendingTool,
                terminateTool
        );
    }

}
