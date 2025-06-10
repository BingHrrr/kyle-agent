package com.kyle.kyleaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * @author Haoran Wang
 * @date 2025/6/10 21:10
 */

/**
 * 创建 SocialAppRagCustomAdvisor的工厂
 */
@Slf4j
public class SocialAppRagCustomAdvisorFactory {
    public static Advisor doCreateSocialAppRagCustomAdvisor(VectorStore vectorStore, String sequence) {
        Filter.Expression expression = new FilterExpressionBuilder().eq("sequence", sequence).build();
        // 创建文档检索器
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .filterExpression(expression) //过滤条件
                .similarityThreshold(0.6) //相似度阈值
                .topK(3) //返回文档数量
                .build();
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever) // 文档检索器
                .queryAugmenter(SocialAppContextualQueryAugmenterFactory.createInstance()) // 查询增强器
                .build();
    }
}
