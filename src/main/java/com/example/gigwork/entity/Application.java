package com.example.gigwork.entity;

import com.example.gigwork.enums.ApplicationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 지원서 엔티티
 * DB 테이블: applications
 * 연관 테이블:
 *   - jobs (N:1) - 공고 정보
 *   - jobseeker_profiles (N:1) - 구직자 프로필 정보
 */
@Entity
@Table(name = "applications")
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // N:1 관계 - Job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    // N:1 관계 - JobseekerProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobseeker_id", nullable = false)
    private JobseekerProfile jobseeker;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    
    private Integer suitability;
    
    @Column(name = "applied_date", nullable = false, updatable = false)
    private LocalDateTime appliedDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (appliedDate == null) {
            appliedDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 기본 생성자
    public Application() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Job getJob() {
        return job;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    public JobseekerProfile getJobseeker() {
        return jobseeker;
    }
    
    public void setJobseeker(JobseekerProfile jobseeker) {
        this.jobseeker = jobseeker;
    }
    
    public ApplicationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ApplicationStatus status) {
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
