package com.example.gigwork.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 사업자 프로필 엔티티
 * DB 테이블: employer_profiles
 * 연관 테이블:
 *   - users (1:1) - 사용자 정보
 *   - jobs (1:N) - 일자리 공고 목록
 */
@Entity
@Table(name = "employer_profiles")
public class EmployerProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 1:1 관계 - User
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
    
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    private String phone;
    
    private String address;
    
    @Column(name = "business_number", unique = true)
    private String businessNumber;
    
    @Column(name = "representative_name")
    private String representativeName;
    
    private String industry;
    
    @Column(name = "company_size")
    private String companySize;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // 1:N 관계 - 일자리 공고
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();
    
    // 기본 생성자
    public EmployerProfile() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getCompanySize() {
        return companySize;
    }
    
    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<Job> getJobs() {
        return jobs;
    }
    
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
