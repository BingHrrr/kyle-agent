package com.kyle.kyleaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

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
