package com.kyle.kyleaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
class WebScrapingToolTest {

    @Test
    void scrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.leetcode.cn";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
    }
}