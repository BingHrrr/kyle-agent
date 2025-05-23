package com.kyle.kyleaiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
@ServletComponentScan
public class KyleAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(KyleAiAgentApplication.class, args);
    }

}
