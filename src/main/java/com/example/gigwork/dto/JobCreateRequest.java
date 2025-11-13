package com.example.gigwork.dto;

import java.time.LocalDate;
import java.util.List;

public class JobCreateRequest {
    
    private String title;
    private String category;
    private String company;
    private String location;
    private String description;
    private List<String> qualifications;
    private List<String> requirements;
    private String otherRequirement;
    private List<String> workingDays;  // ["2025-01-15", "2025-01-16", ...]
    private String startTime;  // "09:00"
    private String endTime;    // "18:00"
    private String salary;
    private String salaryType;
    private LocalDate deadline;
    private String gender;
    private String age;
    private String education;
    
    // 기본 생성자
    public JobCreateRequest() {}
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> getQualifications() {
        return qualifications;
    }
    
    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }
    
    public List<String> getRequirements() {
        return requirements;
    }
    
    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }
    
    public String getOtherRequirement() {
        return otherRequirement;
    }
    
    public void setOtherRequirement(String otherRequirement) {
        this.otherRequirement = otherRequirement;
    }
    
    public List<String> getWorkingDays() {
        return workingDays;
    }
    
    public void setWorkingDays(List<String> workingDays) {
        this.workingDays = workingDays;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getSalary() {
        return salary;
    }
    
    public void setSalary(String salary) {
        this.salary = salary;
    }
    
    public String getSalaryType() {
        return salaryType;
    }
    
    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }
    
    public LocalDate getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getAge() {
        return age;
    }
    
    public void setAge(String age) {
        this.age = age;
    }
    
    public String getEducation() {
        return education;
    }
    
    public void setEducation(String education) {
        this.education = education;
    }
}
