package com.kyle.kyleaiagent.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyle.kyleaiagent.mapper.ConversationMemoryMapper;
import com.kyle.kyleaiagent.model.dto.ConversationMemory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Haoran Wang
 * @date 2025/6/5 20:41
 */
@Component
public class ConversationMemoryDAO extends ServiceImpl<ConversationMemoryMapper, ConversationMemory> {


    public List<ConversationMemory> getMessages(String conversationId) {
        return this.lambdaQuery()
                .eq(ConversationMemory::getConversationId, conversationId)
                .list();
    }

    public boolean deleteMemory(String conversationId) {
        return this.lambdaUpdate()
                .eq(ConversationMemory::getConversationId, conversationId)
                .remove();
    }
}

