package com.example.gigwork.dto;

public class AuthResponse {
    
    private Long userId;
    private String email;
    private String userType;
    private String message;
    
    // 기본 생성자
    public AuthResponse() {}
    
    // 전체 생성자
    public AuthResponse(Long userId, String email, String userType, String message) {
        this.userId = userId;
        this.email = email;
        this.userType = userType;
        this.message = message;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
