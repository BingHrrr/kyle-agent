package com.kyle.kyleaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Haoran Wang
 * @date 2025/6/15 20:50
 */

/**
 * OpenManus-main/app/agent/base.py
 * ref: "<a href="https://github.com/FoundationAgents/OpenManus">...</a>"
 */
@Data
@Slf4j
public abstract class BaseAgent {
    // Core attributes
    private String name;

    //Prompts
    private String systemPrompt;
    private String nextStepPrompt;

    // state
    private AgentState state = AgentState.IDLE;

    //Execution control
    private int maxSteps = 10;
    private int currentStep = 0;

    // LLM
    private ChatClient chatClient;

    // Memory 需要自己维护
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行agent
     *
     * @param userPrompt 用户prompt
     * @return 执行结果
     */
    public String run(String userPrompt) {
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state: " + this.state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("UserPrompt cannot be empty");
        }
        // 修改状态
        state = AgentState.RUNNING;
        // 保存消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 结果列表
        List<String> results = new ArrayList<>();
        try {
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNum = i + 1;
                currentStep = stepNum;
                log.info("Step #" + stepNum + ": " + currentStep);
                // 单步执行
                String stepResult = step();
                String result = "Executing Step: " + stepNum + ", " + stepResult;
                results.add(result);
            }
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max steps " + maxSteps);
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Error executing agent", e);
            return "Error executing agent: " + e.getMessage();
        } finally {
            this.cleanup();
        }
    }
    /**
     * 运行agent（SseEmitter）
     *
     * @param userPrompt 用户prompt
     * @return 流式返回执行结果
     */
    public SseEmitter runStream(String userPrompt) {
        SseEmitter sseEmitter = new SseEmitter(300000L);
        CompletableFuture.runAsync(()->{
            try {
                if (this.state != AgentState.IDLE) {
                    sseEmitter.send("Cannot run agent from state: " + this.state);
                    sseEmitter.complete();
                    return;
                }
                if (StrUtil.isBlank(userPrompt)) {
                    sseEmitter.send("Cannot run agent because UserPrompt is empty");
                    sseEmitter.complete();
                    return;
                }
                // 修改状态
                state = AgentState.RUNNING;
                // 保存消息上下文
                messageList.add(new UserMessage(userPrompt));
                try {
                    for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                        int stepNum = i + 1;
                        currentStep = stepNum;
                        log.info("Step #" + stepNum + ": " + currentStep);
                        // 单步执行
                        String stepResult = step();
//                        String result = "Executing Step: " + stepNum + ", " + stepResult;
                        String result = "Executing Step: " + stepNum + "\n";
                        // 发送每一步的结果
                        List<Message> currentMessageList = getMessageList();
//                        if ()
                        Message message = currentMessageList.get(currentMessageList.size() - 2);
                        if(message instanceof AssistantMessage){
                            String returnMsg = ((AssistantMessage)message).getText();
                            sseEmitter.send(result + returnMsg);
                        } else{
                            sseEmitter.send("正在处理....");
                        }
//                        String returnMsg = message.getText();
//                        sseEmitter.send(result + returnMsg);
                    }
                    if (currentStep >= maxSteps) {
                        state = AgentState.FINISHED;
                        sseEmitter.send("Terminated: Reached max steps " + maxSteps);
                    }
                    // 正常完成run
                    sseEmitter.complete();
                } catch (Exception e) {
                    state = AgentState.ERROR;
                    log.error("Error executing agent", e);
                    try {
                        sseEmitter.send("Error executing agent: " + e.getMessage());
                        sseEmitter.complete();
                    } catch (Exception ex) {
                        sseEmitter.completeWithError(ex);
                    }
                } finally {
                    this.cleanup();
                }
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });
        // 设置超时回调方法
        sseEmitter.onTimeout(()->{
            this.state = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timed out");
        });

        // 完成回调
        sseEmitter.onCompletion(()->{
            if(this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE Connection Completed");
        });
        return sseEmitter;
    }
    /**
     * 执行（模板方法设计模式）
     *
     * @return 单步执行结果
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected  void cleanup() {

    };

}
