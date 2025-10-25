package com.shodh.shodh_a_code.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "submissions")
public class Submission {
    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("problem_id")
    private String problemId;

    @Field("contest_id")
    private String contestId;

    private String code;
    private String language;
    private SubmissionStatus status;
    private String result;
    private String errorMessage;

    @Field("execution_time")
    private Long executionTime; // in milliseconds

    @Field("memory_used")
    private Long memoryUsed; // in bytes

    private LocalDateTime submittedAt;
    private LocalDateTime processedAt;

    public enum SubmissionStatus {
        PENDING, RUNNING, ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED,
        MEMORY_LIMIT_EXCEEDED, RUNTIME_ERROR, COMPILATION_ERROR
    }

    public enum Language {
        JAVA, PYTHON, CPP, C
    }

    // Constructors
    public Submission() {
        this.submittedAt = LocalDateTime.now();
        this.status = SubmissionStatus.PENDING;
    }

    public Submission(String userId, String problemId, String contestId, String code, String language) {
        this();
        this.userId = userId;
        this.problemId = problemId;
        this.contestId = contestId;
        this.code = code;
        this.language = language;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Long getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
