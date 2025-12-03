package com.example.gigwork.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.gigwork.enums.JobStatus;

import jakarta.persistence.*;

/**
 * 일자리 공고 엔티티
 * DB 테이블: jobs
 * 연관 테이블:
 *   - employer_profiles (N:1) - 사업자 프로필 정보
 *   - applications (1:N) - 지원서 목록
 */
@Entity
@Table(name = "jobs")
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // N:1 관계 - EmployerProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private EmployerProfile employer;
    
    @Column(nullable = false)
    private String title;
    
    private String company;
    
    private String location;
    
    @Column(name = "address_detail")
    private String addressDetail;
    
    private String salary;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String category;
    
    @Column(name = "job_type")
    private String jobType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.ACTIVE;
    
    @Column(name = "posted_date")
    private LocalDate postedDate;
    
    private LocalDate deadline;
    
    @Column(name = "work_hours")
    private String workHours;
    
    @Column(name = "work_days", columnDefinition = "TEXT")
    private String workDays;
    
    @Column(columnDefinition = "TEXT")
    private String qualifications;  // JSON array로 저장
    
    @Column(columnDefinition = "TEXT")
    private String requirements;  // JSON array로 저장
    
    @Column(name = "other_requirement")
    private String otherRequirement;
    
    @Column(name = "salary_type")
    private String salaryType;
    
    @Column(name = "start_time")
    private String startTime;
    
    @Column(name = "end_time")
    private String endTime;
    
    private String gender;
    
    private String age;
    
    private String education;
    
    @Column(nullable = false)
    private Integer views = 0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 1:N 관계 - 지원서
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (postedDate == null) {
            postedDate = LocalDate.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 기본 생성자
    public Job() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public EmployerProfile getEmployer() {
        return employer;
    }
    
    public void setEmployer(EmployerProfile employer) {
        this.employer = employer;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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
    
    public String getAddressDetail() {
        return addressDetail;
    }
    
    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }
    
    public String getSalary() {
        return salary;
    }
    
    public void setSalary(String salary) {
        this.salary = salary;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    
    public JobStatus getStatus() {
        return status;
    }
    
    public void setStatus(JobStatus status) {
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
    
    public String getWorkHours() {
        return workHours;
    }
    
    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }
    
    public String getWorkDays() {
        return workDays;
    }
    
    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }
    
    public Integer getViews() {
        return views;
    }
    
    public void setViews(Integer views) {
        this.views = views;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public List<Application> getApplications() {
        return applications;
    }
    
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
    
    public String getQualifications() {
        return qualifications;
    }
    
    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    
    public String getRequirements() {
        return requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
    public String getOtherRequirement() {
        return otherRequirement;
    }
    
    public void setOtherRequirement(String otherRequirement) {
        this.otherRequirement = otherRequirement;
    }
    
    public String getSalaryType() {
        return salaryType;
    }
    
    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
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
