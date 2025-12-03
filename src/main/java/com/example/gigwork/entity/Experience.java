package com.example.gigwork.entity;

import jakarta.persistence.*;

/**
 * 경력 엔티티
 * DB 테이블: experiences
 * 연관 테이블:
 *   - jobseeker_profiles (N:1) - 구직자 프로필 정보
 */
@Entity
@Table(name = "experiences")
public class Experience {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // N:1 관계 - JobseekerProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobseeker_id", nullable = false)
    private JobseekerProfile jobseeker;
    
    @Column(nullable = false)
    private String company;
    
    private String position;
    
    @Column(name = "start_date")
    private String startDate;
    
    @Column(name = "end_date")
    private String endDate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // 기본 생성자
    public Experience() {}
    
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
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
