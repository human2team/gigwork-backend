package com.example.gigwork.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * 저장된 일자리 엔티티
 * DB 테이블: saved_jobs
 * 연관 테이블:
 *   - jobseeker_profiles (N:1) - 구직자 프로필 정보
 *   - jobs (N:1) - 공고 정보
 */
@Entity
@Table(name = "saved_jobs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"jobseeker_id", "job_id"})
})
public class SavedJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "jobseeker_id", nullable = false)
    private JobseekerProfile jobseeker;
    
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @Column(name = "saved_date", nullable = false)
    private LocalDateTime savedDate;
    
    @PrePersist
    protected void onCreate() {
        savedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public JobseekerProfile getJobseeker() {
        return jobseeker;
    }
    
    public void setJobseeker(JobseekerProfile jobseeker) {
        this.jobseeker = jobseeker;
    }
    
    public Job getJob() {
        return job;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    public LocalDateTime getSavedDate() {
        return savedDate;
    }
    
    public void setSavedDate(LocalDateTime savedDate) {
        this.savedDate = savedDate;
    }
}
