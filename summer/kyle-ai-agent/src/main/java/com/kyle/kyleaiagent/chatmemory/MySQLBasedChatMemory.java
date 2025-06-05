package com.kyle.kyleaiagent.chatmemory;

import cn.hutool.core.collection.CollectionUtil;
import com.google.gson.Gson;
import com.kyle.kyleaiagent.dao.ConversationMemoryDAO;
import com.kyle.kyleaiagent.model.dto.ConversationMemory;
import com.kyle.kyleaiagent.model.enums.MessageTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haoran Wang
 * @date 2025/6/5 20:41
 */
@Component
@RequiredArgsConstructor
public class MySQLBasedChatMemory implements ChatMemory {

    private final ConversationMemoryDAO conversationMemoryDAO;


    @Override
    public void add(String conversationId, List<Message> messages) {
        Gson gson = new Gson();
        List<ConversationMemory> conversationMemories = messages.stream().map(message -> {
            String messageType = message.getMessageType().getValue();
            String mes = gson.toJson(message);
            return ConversationMemory.builder().conversationId(conversationId)
                    .type(messageType).memory(mes).build();
        }).toList();
        conversationMemoryDAO.saveBatch(conversationMemories);
    }


    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<ConversationMemory> messages = conversationMemoryDAO.getMessages(conversationId);
        if (CollectionUtil.isEmpty(messages)) {
            return List.of();
        }
        return messages.stream()
                .skip(Math.max(0, messages.size() - lastN))
                .map(this::getMessage)
                .collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        conversationMemoryDAO.deleteMemory(conversationId);
    }

    private Message getMessage(ConversationMemory conversationMemory) {
        String memory = conversationMemory.getMemory();
        Gson gson = new Gson();
        return (Message) gson.fromJson(memory, MessageTypeEnum.fromValue(conversationMemory.getType()).getClazz());
    }
}
