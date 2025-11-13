package com.example.gigwork.dto;

import java.time.LocalDate;
import java.util.List;

public class JobDetailResponse {
    
    private Long id;
    private Long employerId;
    private String title;
    private String category;
    private String company;
    private String location;
    private String description;
    private List<String> qualifications;
    private List<String> requirements;
    private String otherRequirement;
    private List<String> workingDays;
    private String startTime;
    private String endTime;
    private String salary;
    private String salaryType;
    private String status;
    private LocalDate postedDate;
    private LocalDate deadline;
    private Integer views;
    private Integer applicants;
    private String gender;
    private String age;
    private String education;
    
    // 기본 생성자
    public JobDetailResponse() {}
    
    // 전체 생성자
    public JobDetailResponse(Long id, Long employerId, String title, String category, String company, 
                           String location, String description, List<String> qualifications,
                           List<String> requirements, String otherRequirement,
                           List<String> workingDays, String startTime, String endTime,
                           String salary, String salaryType, String status,
                           LocalDate postedDate, LocalDate deadline, Integer views, Integer applicants) {
        this.id = id;
        this.employerId = employerId;
        this.title = title;
        this.category = category;
        this.company = company;
        this.location = location;
        this.description = description;
        this.qualifications = qualifications;
        this.requirements = requirements;
        this.otherRequirement = otherRequirement;
        this.workingDays = workingDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.salary = salary;
        this.salaryType = salaryType;
        this.status = status;
        this.postedDate = postedDate;
        this.deadline = deadline;
        this.views = views;
        this.applicants = applicants;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getEmployerId() {
        return employerId;
    }
    
    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }
    
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDate getPostedDate() {
        return postedDate;
    }
    
    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }
    
    public LocalDate getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    
    public Integer getViews() {
        return views;
    }
    
    public void setViews(Integer views) {
        this.views = views;
    }
    
    public Integer getApplicants() {
        return applicants;
    }
    
    public void setApplicants(Integer applicants) {
        this.applicants = applicants;
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
