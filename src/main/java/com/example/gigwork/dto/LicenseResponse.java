package com.example.gigwork.dto;

import java.time.LocalDate;

public class LicenseResponse {
    private Long id;
    private String name;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    
    // 생성자
    public LicenseResponse() {}
    
    public LicenseResponse(Long id, String name, LocalDate issueDate, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
