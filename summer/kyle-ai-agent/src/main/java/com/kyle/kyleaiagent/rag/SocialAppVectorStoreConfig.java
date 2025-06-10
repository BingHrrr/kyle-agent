package com.kyle.kyleaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Haoran Wang
 * @date 2025/6/5 16:26
 */

/**
 * 实现基于内存的向量存储 SimpleVectorStore
 */
@Configuration
public class SocialAppVectorStoreConfig {

    @Resource
    private SocialAppDocumentLoader socialAppDocumentLoader;
    @Resource
    private MyKeyWordEnricher keyWordEnricher;

    @Bean
    VectorStore socialAppVectorStore(EmbeddingModel dashscopeEmbeddingModel, MyTokenSplitter myTokenSplitter) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documents = socialAppDocumentLoader.loadSocialApps();
        // 自定义分割
//        List<Document> splitDocuments = myTokenSplitter.splitDocuments(documents);
        // 自定义元信息增强keyWordEnricher
//        List<Document> enrichedDocuments = keyWordEnricher.enrichDocuments(documents);
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }

}
