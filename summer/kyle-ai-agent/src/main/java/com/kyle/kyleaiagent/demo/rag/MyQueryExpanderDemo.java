package com.kyle.kyleaiagent.demo.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Haoran Wang
 * @date 2025/6/10 20:31
 */

/**
 *  多查询扩展
 *  用于提高召回率
 */
@Component
public class MyQueryExpanderDemo {
    private final ChatClient.Builder chatClientBuilder;

    public MyQueryExpanderDemo(ChatModel dashscopeChatModel) {
        this.chatClientBuilder = ChatClient.builder(dashscopeChatModel);
    }

    public List<Query> expand(String query) {
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = multiQueryExpander.expand(new Query(query));
        return queries;
    }
}
