package com.kyle.kyleaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.kyle.kyleaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haoran Wang
 * @date 2025/6/15 20:50
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class ToolCallAgent extends ReActAgent {
    // 可用工具列表
    private final ToolCallback[] availableTools;

    // 保存工具调用的响应
    private ChatResponse toolCallChatResponse;

    // 工具调用的管理者
    private final ToolCallingManager toolCallingManager;

    // 手动维护上下文，禁用内置的工具调用机制
    private ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                // 手动维护上下文，禁用内置的工具调用机制
                .withProxyToolCalls(true)
                .build();
    }

    @Override
    public boolean think() {
        if (getNextStepPrompt() != null && !getNextStepPrompt().isEmpty()) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }
        List<Message> messageList = getMessageList();
        Prompt prompt = new Prompt(messageList, chatOptions);
        try {
            ChatResponse chatResponse = getChatClient()
                    .prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call().chatResponse();
            // 记录响应 用于Act
            this.toolCallChatResponse = chatResponse;
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 输出提示信息
            String result = assistantMessage.getText();
            //
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            log.info(getName() + "的思考：" + result);
            log.info(getName() + "选择了" + toolCallList.size() + "个工具进行调用");
            String toolCallInfo = toolCallList.stream().map(toolCall ->
                    String.format("工具名称：%s, 工具参数：%s", toolCall.name(), toolCall.arguments())
            ).collect(Collectors.joining("\n"));
            log.info(toolCallInfo);
            if (toolCallList.isEmpty()) {
                // 不调用工具，直接传入assistantMessage
                getMessageList().add(assistantMessage);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "思考中遇到问题：" + e.getMessage());
            getMessageList().add(new AssistantMessage("处理时遇到错误" + e.getMessage()));
            return false;
        }
    }

    @Override
    public String act() {
        if (!toolCallChatResponse.hasToolCalls()) {
            return "无工具调用";
        }
        // 调用工具
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        // 工具调用结果
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);
        /**
         * 记录历史消息 ToolCallingManager实现类DefaultToolCallingManager实现的的 executeToolCalls()方法中
         * 已经默认添加了AssistantMessage以及工具调用结果到ToolExecutionResult
         */
        setMessageList(toolExecutionResult.conversationHistory());
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        String results = toolResponseMessage.getResponses().stream()
                .map(response -> "工具 " + response.name() + " 完成任务，结果：" + response.responseData())
                .collect(Collectors.joining("\n"));
        // 判断是否调用了终止工具
        boolean ifCallTerminateTool = toolResponseMessage.getResponses().stream()
                .anyMatch(response -> "doTerminate".equals(response.name()));
        if (ifCallTerminateTool) {
            setState(AgentState.FINISHED);
        }
        log.info(results);
        return results;
    }
}
