package com.shodh.shodh_a_code.dto;

public class SubmissionRequest {
    private String userId;
    private String problemId;
    private String contestId;
    private String code;
    private String language;

    // Constructors
    public SubmissionRequest() {
    }

    public SubmissionRequest(String userId, String problemId, String contestId, String code, String language) {
        this.userId = userId;
        this.problemId = problemId;
        this.contestId = contestId;
        this.code = code;
        this.language = language;
    }

    // Getters and Setters
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
}
