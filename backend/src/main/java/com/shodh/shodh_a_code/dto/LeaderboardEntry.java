package com.shodh.shodh_a_code.dto;

public class LeaderboardEntry {
    private String userId;
    private String username;
    private int totalSubmissions;
    private int acceptedSubmissions;
    private int totalScore;
    private long totalTime; // in milliseconds

    // Constructors
    public LeaderboardEntry() {
    }

    public LeaderboardEntry(String userId, String username, int totalSubmissions, int acceptedSubmissions,
            int totalScore, long totalTime) {
        this.userId = userId;
        this.username = username;
        this.totalSubmissions = totalSubmissions;
        this.acceptedSubmissions = acceptedSubmissions;
        this.totalScore = totalScore;
        this.totalTime = totalTime;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(int totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public int getAcceptedSubmissions() {
        return acceptedSubmissions;
    }

    public void setAcceptedSubmissions(int acceptedSubmissions) {
        this.acceptedSubmissions = acceptedSubmissions;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
