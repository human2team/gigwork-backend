package com.example.gigwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gigwork.entity.EmployerProfile;
import com.example.gigwork.repository.EmployerProfileRepository;

@RestController
@RequestMapping("/api/employer/profile")
public class EmployerProfileController {
    
    @Autowired
    private EmployerProfileRepository employerProfileRepository;
    
    /**
     * 사업자 프로필 조회 (간단 버전 - 회사명만)
     * GET /api/employer/profile/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable("userId") Long userId) {
        try {
            EmployerProfile profile = employerProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));
            
            // 간단한 응답 (회사명만)
            SimpleProfileResponse response = new SimpleProfileResponse(
                profile.getId(),
                profile.getCompanyName()
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 간단한 프로필 응답 DTO
     */
    static class SimpleProfileResponse {
        private Long id;
        private String companyName;
        
        public SimpleProfileResponse(Long id, String companyName) {
            this.id = id;
            this.companyName = companyName;
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getCompanyName() {
            return companyName;
        }
        
        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
    }
    
    /**
     * 에러 응답용 내부 클래스
     */
    static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
