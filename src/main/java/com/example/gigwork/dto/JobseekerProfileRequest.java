package com.example.gigwork.dto;

import com.example.gigwork.enums.MuscleStrength;
import java.time.LocalDate;

public class JobseekerProfileRequest {
    private String name;
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
    
    // Getters and Setters
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
