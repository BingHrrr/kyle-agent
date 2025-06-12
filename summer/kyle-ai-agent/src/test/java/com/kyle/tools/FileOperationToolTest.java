package com.kyle.tools;

import com.kyle.kyleaiagent.tools.FileOperationTool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileOperationToolTest {

    @Test
    void readFile() {
        String fileName = "test.txt";
        String content = new FileOperationTool().readFile(fileName);
        Assertions.assertNotNull(content);
    }

    @Test
    void writeFile() {
        String fileName = "test.txt";
        String fileContent = "This is a test file";
        String result = new FileOperationTool().writeFile(fileName, fileContent);
        Assertions.assertNotNull(result);
    }
}