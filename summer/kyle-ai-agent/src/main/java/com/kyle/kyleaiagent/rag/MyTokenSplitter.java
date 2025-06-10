package com.kyle.kyleaiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Haoran Wang
 * @date 2025/6/10 20:24
 */
@Component
public class MyTokenSplitter {
    /**
     * 默认参数的分割
     * @param documents 读取的文档
     * @return 分割后的文档
     */
    public List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        return tokenTextSplitter.apply(documents);
    }
    /**
     * 调节参数的分割
     * @param documents 读取的文档
     * @return 分割后的文档
     */
    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(200, 100, 10, 5000, true);
        return splitter.apply(documents);
    }


}
