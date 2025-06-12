package com.kyle.kyleaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void searchOnWeb() {
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "Spring AI 官网？";
        String result = tool.searchWeb(query);
        assertNotNull(result);
    }
}