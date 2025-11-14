package com.example.gigwork.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gigwork.dto.RecommendationResponse;
import com.example.gigwork.entity.Job;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.enums.JobStatus;
import com.example.gigwork.exception.ApiException;
import com.example.gigwork.repository.JobRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;

@Service
public class RecommendationService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;

    /**
     * 사용자 프로필 기반 추천 공고 목록 조회
     */
    @Transactional(readOnly = true)
    public List<RecommendationResponse> getRecommendations(Long userId) {
        // 사용자 프로필 조회
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        // 활성화된 모든 공고 조회
        List<Job> activeJobs = jobRepository.findByStatus(JobStatus.ACTIVE);

        // 각 공고에 대한 적합도 계산 및 응답 생성
        List<RecommendationResponse> recommendations = activeJobs.stream()
                .map(job -> {
                    int suitability = calculateSuitability(profile, job);
                    return convertToRecommendationResponse(job, suitability);
                })
                .sorted(Comparator.comparing(RecommendationResponse::getSuitability).reversed()) // 적합도 높은 순
                .collect(Collectors.toList());

        return recommendations;
    }

    /**
     * Job Entity -> RecommendationResponse DTO 변환
     */
    private RecommendationResponse convertToRecommendationResponse(Job job, int suitability) {
        RecommendationResponse response = new RecommendationResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCategory(job.getCategory());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setSalary(job.getSalary());
        response.setSalaryType(job.getSalaryType());
        response.setDescription(job.getDescription());
        response.setSuitability(suitability);
        return response;
    }

    /**
     * 적합도 계산 (ApplicationService와 동일한 로직)
     */
    private int calculateSuitability(JobseekerProfile profile, Job job) {
        int score = 0;
        
        // 1. 성별 매칭 (20점)
        if (job.getGender() == null || job.getGender().equals("무관") || job.getGender().isEmpty()) {
            score += 20;
        } else if (profile.getGender() != null && profile.getGender().equals(job.getGender())) {
            score += 20;
        }
        
        // 2. 연령 매칭 (20점)
        if (job.getAge() == null || job.getAge().equals("무관") || job.getAge().isEmpty()) {
            score += 20;
        } else if (profile.getBirthDate() != null) {
            int age = Period.between(profile.getBirthDate(), LocalDate.now()).getYears();
            String ageGroup = getAgeGroup(age);
            if (ageGroup.equals(job.getAge())) {
                score += 20;
            } else if (isAdjacentAgeGroup(ageGroup, job.getAge())) {
                score += 10;
            }
        }
        
        // 3. 학력 매칭 (20점)
        if (job.getEducation() == null || job.getEducation().equals("무관") || job.getEducation().isEmpty()) {
            score += 20;
        } else if (profile.getEducation() != null) {
            int profileLevel = getEducationLevel(profile.getEducation());
            int jobLevel = getEducationLevel(job.getEducation());
            if (profileLevel >= jobLevel) {
                score += 20;
            } else if (profileLevel == jobLevel - 1) {
                score += 10;
            }
        }
        
        // 4. 자격증 보유 (20점)
        if (profile.getLicenses() != null && !profile.getLicenses().isEmpty()) {
            score += 20;
        } else {
            score += 5;
        }
        
        // 5. 경력 보유 (20점)
        if (profile.getExperiences() != null && !profile.getExperiences().isEmpty()) {
            score += 20;
        } else {
            score += 5;
        }
        
        return Math.min(100, Math.max(0, score));
    }
    
    private String getAgeGroup(int age) {
        if (age < 30) return "20대";
        if (age < 40) return "30대";
        if (age < 50) return "40대";
        if (age < 60) return "50대";
        return "60대 이상";
    }
    
    private boolean isAdjacentAgeGroup(String group1, String group2) {
        String[] ageGroups = {"20대", "30대", "40대", "50대", "60대 이상"};
        int index1 = -1, index2 = -1;
        for (int i = 0; i < ageGroups.length; i++) {
            if (ageGroups[i].equals(group1)) index1 = i;
            if (ageGroups[i].equals(group2)) index2 = i;
        }
        return Math.abs(index1 - index2) == 1;
    }
    
    private int getEducationLevel(String education) {
        if (education == null) return 0;
        if (education.contains("고졸")) return 1;
        if (education.contains("대졸") || education.contains("대학")) return 2;
        if (education.contains("석사")) return 3;
        if (education.contains("박사")) return 4;
        return 0;
    }
}
