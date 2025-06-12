package com.kyle.kyleaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "testPdf.pdf";
        String content = "this is for test pdf https://github.com/";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}