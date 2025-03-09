package com.rhotiz.kafka.errorhandlers.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MyCommand {
    private String executionId;
    private int commandId;
    private int failureCount;

    public MyCommand() {
    }

    public MyCommand(String executionId, int commandId, int failureCount) {
        this.executionId = executionId;
        this.commandId = commandId;
        this.failureCount = failureCount;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    @Override
    public String toString() {
        return String.format("%s-%s-Err%s", executionId, commandId, failureCount);
    }
}