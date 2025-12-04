package com.example.gigwork.entity;

import com.example.gigwork.enums.UserType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 사용자 엔티티
 * DB 테이블: users
 * 연관 테이블:
 *   - jobseeker_profiles (1:1) - 구직자 프로필 정보
 *   - employer_profiles (1:1) - 사업자 프로필 정보
 *   - refresh_tokens (1:N) - 리프레시 토큰 목록
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "refresh_token", length = 512)
    private String refreshToken;
    
    @Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;
    
    // 1:1 관계 - 구직자 프로필
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private JobseekerProfile jobseekerProfile;
    
    // 1:1 관계 - 고용주 프로필
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmployerProfile employerProfile;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 기본 생성자
    public User() {}
    
    // 생성자
    public User(String email, String password, UserType userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public JobseekerProfile getJobseekerProfile() {
        return jobseekerProfile;
    }
    
    public void setJobseekerProfile(JobseekerProfile jobseekerProfile) {
        this.jobseekerProfile = jobseekerProfile;
    }
    
    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }
    
    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public LocalDateTime getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }
    
    public void setRefreshTokenExpiry(LocalDateTime refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }
}
