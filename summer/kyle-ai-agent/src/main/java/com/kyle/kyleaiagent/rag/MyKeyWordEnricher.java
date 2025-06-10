package com.kyle.kyleaiagent.rag;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Haoran Wang
 * @date 2025/6/10 10:37
 */

/**
 * 自定义元信息增强器（补充元信息）
 * 调用AI进行关键词提取（*速度较慢）
 */
@Component
public class MyKeyWordEnricher {

    @Resource
    private DashScopeChatModel dashscopeChatModel;

    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
        List<Document> enrichedDocuments = keywordMetadataEnricher.apply(documents);
        return enrichedDocuments;
    }
}
