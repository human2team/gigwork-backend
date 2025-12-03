package com.example.gigwork.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.gigwork.enums.MuscleStrength;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * 구직자 프로필 엔티티
 * DB 테이블: jobseeker_profiles
 * 연관 테이블:
 *   - users (1:1) - 사용자 정보
 *   - licenses (1:N) - 자격증 목록
 *   - experiences (1:N) - 경력 목록
 *   - applications (1:N) - 지원서 목록
 */
@Entity
@Table(name = "jobseeker_profiles")
public class JobseekerProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 1:1 관계 - User
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String name;
    
    private String phone;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    private String gender;
    
    private String address;
    
    private String education;
    
    @Column(name = "preferred_region")
    private String preferredRegion;
    
    @Column(name = "preferred_district")
    private String preferredDistrict;
    
    @Column(name = "preferred_dong")
    private String preferredDong;
    
    @Column(name = "work_duration")
    private String workDuration;
    
    @Column(name = "work_days")
    private String workDays;
    
    @Column(name = "work_time")
    private String workTime;
    
    private String mbti;
    
    @Column(columnDefinition = "TEXT")
    private String introduction;
    
    // 희망 업직종 (소분류) - 코드/명 콤마 구분 저장
    @Column(name = "desired_category_codes", columnDefinition = "TEXT")
    private String desiredCategoryCodes;

    @Column(name = "desired_category_names", columnDefinition = "TEXT")
    private String desiredCategoryNames;
    
    // 나의 강점 (쉼표로 구분된 문자열)
    private String strengths;
    
    // 신체 정보
    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_strength")
    private MuscleStrength muscleStrength;
    
    private Integer height;
    
    private Integer weight;
    
    // 1:N 관계 - 자격증
    @OneToMany(mappedBy = "jobseeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<License> licenses = new ArrayList<>();
    
    // 1:N 관계 - 경력
    @OneToMany(mappedBy = "jobseeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Experience> experiences = new ArrayList<>();
    
    // 1:N 관계 - 지원서
    @OneToMany(mappedBy = "jobseeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Application> applications = new ArrayList<>();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 기본 생성자
    public JobseekerProfile() {}
    
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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

    public String getDesiredCategoryCodes() {
        return desiredCategoryCodes;
    }

    public void setDesiredCategoryCodes(String desiredCategoryCodes) {
        this.desiredCategoryCodes = desiredCategoryCodes;
    }

    public String getDesiredCategoryNames() {
        return desiredCategoryNames;
    }

    public void setDesiredCategoryNames(String desiredCategoryNames) {
        this.desiredCategoryNames = desiredCategoryNames;
    }
    
    public String getStrengths() {
        return strengths;
    }
    
    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }
    
    public MuscleStrength getMuscleStrength() {
        return muscleStrength;
    }
    
    public void setMuscleStrength(MuscleStrength muscleStrength) {
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
    
    public List<License> getLicenses() {
        return licenses;
    }
    
    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }
    
    public List<Experience> getExperiences() {
        return experiences;
    }
    
    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }
    
    public List<Application> getApplications() {
        return applications;
    }
    
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
