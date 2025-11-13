package com.example.gigwork.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 사업자가 지원자를 조회할 때 사용하는 DTO
 */
public class ApplicantResponse {
    private Long applicationId;
    private Long jobseekerId;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Integer age;
    private String gender;
    private String address;
    private String education;
    private String preferredRegion;
    private String preferredDistrict;
    private String preferredDong;
    private String workDuration;
    private String workDays;
    private String workTime;
    private String mbti;
    private String introduction;
    private String strengths;
    private String muscleStrength;
    private Integer height;
    private Integer weight;
    private String status; // PENDING, ACCEPTED, REJECTED
    private Integer suitability;
    private LocalDateTime appliedDate;
    private String appliedDateFormatted; // 프론트엔드용
    
    // 자격증 및 경력 정보
    private List<LicenseInfo> licenses;
    private List<ExperienceInfo> experiences;

    public ApplicantResponse() {}
    
    // 내부 클래스: 자격증 정보
    public static class LicenseInfo {
        private Long id;
        private String name;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        
        public LicenseInfo() {}
        
        public LicenseInfo(Long id, String name, LocalDate issueDate, LocalDate expiryDate) {
            this.id = id;
            this.name = name;
            this.issueDate = issueDate;
            this.expiryDate = expiryDate;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public LocalDate getIssueDate() { return issueDate; }
        public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
        public LocalDate getExpiryDate() { return expiryDate; }
        public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    }
    
    // 내부 클래스: 경력 정보
    public static class ExperienceInfo {
        private Long id;
        private String company;
        private String position;
        private String startDate;
        private String endDate;
        private String description;
        
        public ExperienceInfo() {}
        
        public ExperienceInfo(Long id, String company, String position, String startDate, String endDate, String description) {
            this.id = id;
            this.company = company;
            this.position = position;
            this.startDate = startDate;
            this.endDate = endDate;
            this.description = description;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getJobseekerId() {
        return jobseekerId;
    }

    public void setJobseekerId(Long jobseekerId) {
        this.jobseekerId = jobseekerId;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPreferredRegion() {
        return preferredRegion;
    }

    public void setPreferredRegion(String preferredRegion) {
        this.preferredRegion = preferredRegion;
    }

    public String getPreferredDistrict() {
        return preferredDistrict;
    }

    public void setPreferredDistrict(String preferredDistrict) {
        this.preferredDistrict = preferredDistrict;
    }

    public String getPreferredDong() {
        return preferredDong;
    }

    public void setPreferredDong(String preferredDong) {
        this.preferredDong = preferredDong;
    }

    public String getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(String workDuration) {
        this.workDuration = workDuration;
    }

    public String getWorkDays() {
        return workDays;
    }

    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getMuscleStrength() {
        return muscleStrength;
    }

    public void setMuscleStrength(String muscleStrength) {
        this.muscleStrength = muscleStrength;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSuitability() {
        return suitability;
    }

    public void setSuitability(Integer suitability) {
        this.suitability = suitability;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getAppliedDateFormatted() {
        return appliedDateFormatted;
    }

    public void setAppliedDateFormatted(String appliedDateFormatted) {
        this.appliedDateFormatted = appliedDateFormatted;
    }

    public List<LicenseInfo> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<LicenseInfo> licenses) {
        this.licenses = licenses;
    }

    public List<ExperienceInfo> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceInfo> experiences) {
        this.experiences = experiences;
    }
}
