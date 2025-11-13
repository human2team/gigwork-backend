package com.example.gigwork.dto;

public class EmployerSignupRequest {
    
    private String email;
    private String password;
    private String companyName;
    private String businessNumber;
    private String representativeName;
    private String phone;
    private String address;
    
    // 기본 생성자
    public EmployerSignupRequest() {}
    
    // Getters and Setters
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
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getBusinessNumber() {
        return businessNumber;
    }
    
    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }
    
    public String getRepresentativeName() {
        return representativeName;
    }
    
    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
}
