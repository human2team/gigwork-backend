package com.example.gigwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gigwork.dto.ExperienceRequest;
import com.example.gigwork.dto.ExperienceResponse;
import com.example.gigwork.service.ExperienceService;

@RestController
@RequestMapping("/api/jobseeker/experiences")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ExperienceController {
    
    @Autowired
    private ExperienceService experienceService;
    
    /**
     * 사용자의 모든 경력 조회
     * GET /api/jobseeker/experiences/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<ExperienceResponse>> getExperiences(@PathVariable("userId") Long userId) {
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
            @PathVariable("userId") Long userId,
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
            @PathVariable("experienceId") Long experienceId,
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
    public ResponseEntity<Void> deleteExperience(@PathVariable("experienceId") Long experienceId) {
        try {
            experienceService.deleteExperience(experienceId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
