package com.shodh.shodh_a_code.dto;

public class TestCaseDto {
    private String id;
    private String input;
    private String expectedOutput;
    private boolean isHidden;

    // Constructors
    public TestCaseDto() {
    }

    public TestCaseDto(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.isHidden = false;
    }

    public TestCaseDto(String input, String expectedOutput, boolean isHidden) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.isHidden = isHidden;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
}
