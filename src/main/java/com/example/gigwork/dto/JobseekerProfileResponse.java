package com.example.gigwork.dto;

import com.example.gigwork.enums.MuscleStrength;
import java.time.LocalDate;

public class JobseekerProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String education;
    
    // 희망 근무 조건
    private String preferredRegion;
    private String preferredDistrict;
    private String preferredDong;
    private String workDuration;
    private String workDays;
    private String workTime;
    
    // 신체 속성
    private MuscleStrength muscleStrength;
    private Integer height;
    private Integer weight;
    
    // 기타
    private String strengths;
    private String mbti;
    private String introduction;
    
    // 생성자
    public JobseekerProfileResponse() {}
    
    public JobseekerProfileResponse(Long id, String name, String email, String phone, LocalDate birthDate,
                                   String gender, String address, String education, String preferredRegion, String preferredDistrict,
                                   String preferredDong, String workDuration, String workDays, String workTime,
                                   MuscleStrength muscleStrength, Integer height, Integer weight,
                                   String strengths, String mbti, String introduction) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.education = education;
        this.preferredRegion = preferredRegion;
        this.preferredDistrict = preferredDistrict;
        this.preferredDong = preferredDong;
        this.workDuration = workDuration;
        this.workDays = workDays;
        this.workTime = workTime;
        this.muscleStrength = muscleStrength;
        this.height = height;
        this.weight = weight;
        this.strengths = strengths;
        this.mbti = mbti;
        this.introduction = introduction;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getStrengths() {
        return strengths;
    }
    
    public void setStrengths(String strengths) {
        this.strengths = strengths;
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
}
