package com.example.gigwork.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gigwork.dto.RecommendationResponse;
import com.example.gigwork.entity.Job;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.enums.JobStatus;
import com.example.gigwork.repository.JobRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;

@RestController
@RequestMapping("/api/jobseeker/recommendations")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RecommendationController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;

    /**
     * 사용자 프로필 기반 추천 공고 조회
     * GET /api/jobseeker/recommendations/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getRecommendations(@PathVariable("userId") Long userId) {
        try {
            // 사용자 프로필 조회
            JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));

            // 모든 활성 공고 조회
            List<Job> activeJobs = jobRepository.findByStatus(JobStatus.ACTIVE);

            // 각 공고에 대해 적합도 계산
            List<RecommendationResponse> recommendations = new ArrayList<>();
            for (Job job : activeJobs) {
                int suitability = calculateSuitability(profile, job);
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
                // status, deadline 필드 추가
                String status = job.getStatus() != null ? job.getStatus().name() : "ACTIVE";
                response.setStatus(status);
                response.setDeadline(job.getDeadline());
                recommendations.add(response);
            }

            // 적합도 높은 순으로 정렬
            recommendations.sort(Comparator.comparing(RecommendationResponse::getSuitability).reversed());

            return ResponseEntity.ok(recommendations);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 적합도 계산 (ApplicationService의 로직과 동일)
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
        if (education == null || education.equals("무관")) return 0;
        switch (education) {
            case "고졸": return 1;
            case "대졸": return 2;
            case "석사": return 3;
            case "박사": return 4;
            default: return 0;
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        return error;
    }
}
