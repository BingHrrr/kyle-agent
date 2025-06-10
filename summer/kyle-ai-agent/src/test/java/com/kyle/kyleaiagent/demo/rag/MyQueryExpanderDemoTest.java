package com.kyle.kyleaiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
class MyQueryExpanderDemoTest {


   @Resource
   MyQueryExpanderDemo myQueryExpanderDemo;
    @Test
    void expand() {
        /**
         *谁是Kyleee wang啊啊啊？
         *
         */
        List<Query> queries = myQueryExpanderDemo.expand("谁是Kyleee wang啊啊啊？");
        Assertions.assertNotNull(queries);
    }
}