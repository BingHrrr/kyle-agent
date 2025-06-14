package com.kyle.kyleaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@MapperScan("com.kyle.kyleaiagent.mapper")
class SocialAppTest {
    @Resource
    private SocialApp socialApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // first Round
        String message = "你好，我是kyle";
        String answer = socialApp.doChat(message, chatId);
        // second Round
        String message1 = "你好，我想要了解一些和同学王五的社交的方式？";
        String answer1 = socialApp.doChat(message1, chatId);
        Assertions.assertNotNull(answer1);
        // third Round
        String message2 = "你好，我的同学叫什么来着？帮我回忆一下";
        String answer2 = socialApp.doChat(message2, chatId);
        Assertions.assertNotNull(answer2);

    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // first Round
        String message = "你好，我是kyle，在一些人数较多的场合下，可能不好意思去和别人交流，但我不知道该怎么做";
        SocialApp.SocialReport socialReport = socialApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(socialReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        // first Round
        String message = "你好，在一些会议中，该如何自信的表达自己？";
        String answer = socialApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithCloudRag() {
        String chatId = UUID.randomUUID().toString();
        // first Round
        String message = "你好，在一些聚会中，我插不上话怎么办";
        String answer = socialApp.doChatWithCloudRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        testMessage("我想要给xxxx@163.com发送一个邮件，主题是测试，内容是上海的一些小众地点（无需图片，尽量用较短的语言）");
        // 测试联网搜索问题的答案
        testMessage("周末想带朋友去上海聚餐，推荐几个适合的小众打卡地？");

        // 测试网页抓取：恋爱案例分析
        testMessage("最近和朋友吵架了，看看github(https://github.com)上的朋友是怎么解决矛盾的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的星空图片为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的社交档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘社交计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = socialApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
            String chatId = UUID.randomUUID().toString();
            // 测试地图 MCP
//            String message = "我的好朋友居住在上海静安区，请帮我找到 5 公里内合适的约会地点";
//            String answer =  socialApp.doChatWithMcp(message, chatId);
//            Assertions.assertNotNull(answer);
        // 测试引入自己开发的MCP
        String message = "请给我搜索一些小狗相关的图片";
        String answer = socialApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}