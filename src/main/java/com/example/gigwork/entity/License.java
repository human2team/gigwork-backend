package com.example.gigwork.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * 자격증 엔티티
 * DB 테이블: licenses
 * 연관 테이블:
 *   - jobseeker_profiles (N:1) - 구직자 프로필 정보
 */
@Entity
@Table(name = "licenses")
public class License {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // N:1 관계 - JobseekerProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobseeker_id", nullable = false)
    private JobseekerProfile jobseeker;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "issue_date")
    private LocalDate issueDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    // 기본 생성자
    public License() {}
    
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
