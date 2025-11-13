package com.example.gigwork.dto;

public class ApplicationRequest {
    private Long jobId;
    private Integer suitability;

    public ApplicationRequest() {}

    // Getters and Setters
    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getSuitability() {
        return suitability;
    }

    public void setSuitability(Integer suitability) {
        this.suitability = suitability;
    }
}
