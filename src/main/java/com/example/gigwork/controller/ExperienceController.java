package com.example.gigwork.controller;

import com.example.gigwork.dto.ExperienceRequest;
import com.example.gigwork.dto.ExperienceResponse;
import com.example.gigwork.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobseeker/experiences")
public class ExperienceController {
    
    @Autowired
    private ExperienceService experienceService;
    
    /**
     * 사용자의 모든 경력 조회
     * GET /api/jobseeker/experiences/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<ExperienceResponse>> getExperiences(@PathVariable Long userId) {
        try {
            List<ExperienceResponse> experiences = experienceService.getExperiences(userId);
            return ResponseEntity.ok(experiences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 경력 추가
     * POST /api/jobseeker/experiences/{userId}
     */
    @PostMapping("/{userId}")
    public ResponseEntity<ExperienceResponse> addExperience(
            @PathVariable Long userId,
            @RequestBody ExperienceRequest request) {
        try {
            ExperienceResponse experience = experienceService.addExperience(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(experience);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 경력 수정
     * PUT /api/jobseeker/experiences/{experienceId}
     */
    @PutMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long experienceId,
            @RequestBody ExperienceRequest request) {
        try {
            ExperienceResponse experience = experienceService.updateExperience(experienceId, request);
            return ResponseEntity.ok(experience);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 경력 삭제
     * DELETE /api/jobseeker/experiences/{experienceId}
     */
    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long experienceId) {
        try {
            experienceService.deleteExperience(experienceId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
