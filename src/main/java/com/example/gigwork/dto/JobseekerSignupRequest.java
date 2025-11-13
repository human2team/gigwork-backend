package com.example.gigwork.dto;

public class JobseekerSignupRequest {
    
    private String name;
    private String email;
    private String password;
    private String phone;
    private String birthDate;
    
    // 기본 생성자
    public JobseekerSignupRequest() {}
    
    // 전체 생성자
    public JobseekerSignupRequest(String name, String email, String password, String phone, String birthDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthDate = birthDate;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
