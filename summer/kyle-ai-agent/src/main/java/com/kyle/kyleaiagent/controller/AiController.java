package com.kyle.kyleaiagent.controller;

/**
 * @author Haoran Wang
 * @date 2025/6/16 18:29
 */

import com.kyle.kyleaiagent.agent.KyleManus;
import com.kyle.kyleaiagent.app.SocialApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * AI 服务化
 */
@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    private SocialApp socialApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @GetMapping("/social_app/chat/sync")
    public String doChatWithSocialAppSync(String message, String chatId) {
        return socialApp.doChat(message, chatId);
    }

    /**
     * 流式返回 方式一（Flux<String> + MediaType.TEXT_EVENT_STREAM_VALUE）
     *
     * @param message 消息
     * @param chatId  会话id
     * @return 流式输出结果
     */
    @GetMapping(value = "/social_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSocialAppStream(String message, String chatId) {
        return socialApp.doChatWithStream(message, chatId);
    }

    /**
     * 流式返回 方式二（Flux<ServerSentEvent<String>> 无需 MediaType.TEXT_EVENT_STREAM_VALUE）
     *
     * @param message 消息
     * @param chatId  会话id
     * @return 流式输出结果
     */
    @GetMapping(value = "/social_app/chat/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithSocialAppServerSent(String message, String chatId) {
        return socialApp.doChatWithStream(message, chatId)
                .map(chunk -> ServerSentEvent.builder(chunk).build());
    }

    /**
     * 流式返回 方式三（SseEmitter）
     *
     * @param message 消息
     * @param chatId  会话id
     * @return 流式输出结果
     */
    @GetMapping(value = "/social_app/chat/sse_emitter")
    public SseEmitter doChatWithSocialAppSseEmitter(String message, String chatId) {
        SseEmitter sseEmitter = new SseEmitter(180000L);
        socialApp.doChatWithStream(message, chatId)
                // 监听 Flux<String> 变化
                .subscribe(
                        chunk -> {
                            try {
                                sseEmitter.send(chunk);
                            } catch (Exception e) {
                                sseEmitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        sseEmitter::completeWithError,
                        // 处理完成
                        sseEmitter::complete);
        return sseEmitter;
    }

    /**
     * 流式调用 KyleManus 智能体
     *
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        KyleManus yuManus = new KyleManus(allTools, dashscopeChatModel);
        return yuManus.runStream(message);
    }

}
