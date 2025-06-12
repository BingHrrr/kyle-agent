package com.kyle.kyleaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TerminalOperationToolTest {

    @Test
    void executeTerminalCommand() {
        TerminalOperationTool tool = new TerminalOperationTool();
        String command = "dir";
        String result = tool.executeTerminalCommand(command);
        assertNotNull(result);
    }
}