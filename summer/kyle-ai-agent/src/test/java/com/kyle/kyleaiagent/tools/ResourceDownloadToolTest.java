package com.kyle.kyleaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https:/xxxxx.aliyuncs.com/xxxxx";
        String fileName = "logo.png";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}