package com.kyle.kyleaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KyleManusTest {
    @Resource
    private KyleManus kyleManus;

    @Test
    void run() {
        String userPrompt = """  
                我的朋友居住在杭州市西湖区，请帮我找到 5 公里内合适的聚会地点，  
                并结合一些网络图片，制定一份详细的聚会计划，  
                并以 PDF 格式输出""";
        String answer = kyleManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}


