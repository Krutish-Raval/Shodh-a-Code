package com.shodh.shodh_a_code.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "test_cases")
public class TestCase {
    @Id
    private String id;

    @Field("problem_id")
    private String problemId;

    private String input;

    @Field("expected_output")
    private String expectedOutput;

    private boolean isHidden;
    private LocalDateTime createdAt;

    // Constructors
    public TestCase() {
        this.createdAt = LocalDateTime.now();
        this.isHidden = false;
    }

    public TestCase(String problemId, String input, String expectedOutput) {
        this();
        this.problemId = problemId;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public TestCase(String problemId, String input, String expectedOutput, boolean isHidden) {
        this(problemId, input, expectedOutput);
        this.isHidden = isHidden;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
