package com.kyle.kyleaiagent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication(exclude = { PgVectorStoreAutoConfiguration.class })
@ServletComponentScan
@MapperScan("com.kyle.kyleaiagent.mapper")
public class KyleAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(KyleAiAgentApplication.class, args);
    }

}
