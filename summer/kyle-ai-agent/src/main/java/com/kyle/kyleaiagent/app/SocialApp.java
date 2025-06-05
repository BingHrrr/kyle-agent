package com.kyle.kyleaiagent.app;

import com.kyle.kyleaiagent.advisor.MyLoggerAdvisor;
import com.kyle.kyleaiagent.chatmemory.MySQLBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @author Haoran Wang
 * @date 2025/6/2 10:46
 */
@Slf4j
@Component
public class SocialApp {

    private final ChatClient chatClient;

    @Resource
    private VectorStore socialAppVectorStore;

    @Resource
    private Advisor socialAppRagCloudAdvisor;

    private static final String SYSTEM_PROMPT = "你是「社交大师」AI 情感咨询专家，具备心理学、沟通学双领域专业知识，擅长通过结构化引导帮助用户梳理情感困惑。你的核心目标是：\n" +
            "以朋友般温暖但不失专业的语气建立信任，避免机械感回复；\n" +
            "通过递进式提问挖掘用户未明确表达的深层需求（如情绪触发点、关系背景、行为模式等）；\n" +
            "结合具体场景输出可操作的社交策略，而非空泛建议。";

    /**
     * 初始化SocialApp
     *
     * @param dashscopeChatModel 百炼大模型直接注入，也可以使用ChatClient.Builder
     */
    public SocialApp(ChatModel dashscopeChatModel, MySQLBasedChatMemory mySQLBasedChatMemory) {
        // 基于内存的memory
        // InMemoryChatMemory memory = new InMemoryChatMemory();
        // 基于文件的chatMemory
//        String fileDir = System.getProperty("user.dir") + "/chat-memory";
//        FileBasedChatMemory memory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(mySQLBasedChatMemory),
                        // 自定义Advisor
                        new MyLoggerAdvisor()
                )
                .build();
    }

    /**
     * 基础对话 （支持多轮对话）
     *
     * @param message 用户输入
     * @param chatId  具体某个对话的上下文
     * @return 模型输出结果
     * <p>
     * CHAT_MEMORY_RETRIEVE_SIZE_KEY --> 取历史记录的条数
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt().
                user(message)
                .advisors(ad -> ad.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        log.info("tokens: {}", chatResponse.getMetadata().getUsage());
        return content;
    }

    record SocialReport(String title, List<String> suggestions) {

    }

    /**
     * AI 社交报告 结构化输出
     *
     * @param message
     * @param chatId
     * @return
     */
    public SocialReport doChatWithReport(String message, String chatId) {
        SocialReport socialReport = chatClient.
                prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成社交报告，标题为{用户名}的社交报告，内容为建议列表")
                .user(message)
                .advisors(ad -> ad.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(SocialReport.class);
        log.info("Social Report： {}", socialReport);
        return socialReport;

    }

    /**
     * 基于本地知识库进行问答（RAG）--> QuestionAnswerAdvisor
     *
     * @param message 用户输入
     * @param chatId  具体某个对话的上下文
     * @return 模型输出结果
     */
    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt().
                user(message)
                .advisors(ad -> ad.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new QuestionAnswerAdvisor(socialAppVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        log.info("tokens: {}", chatResponse.getMetadata().getUsage());
        return content;
    }


    /**
     * 基于云知识库进行问答（RAG）--> RetrievalAugmentationAdvisor
     *
     * @param message 用户输入
     * @param chatId  具体某个对话的上下文
     * @return 模型输出结果
     */
    public String doChatWithCloudRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt().
                user(message)
                .advisors(ad -> ad.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 应用增强检索服务
                .advisors(socialAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        log.info("tokens: {}", chatResponse.getMetadata().getUsage());
        return content;
    }

}
