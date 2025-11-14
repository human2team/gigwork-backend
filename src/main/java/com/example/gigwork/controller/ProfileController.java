package com.example.gigwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gigwork.dto.JobseekerProfileRequest;
import com.example.gigwork.dto.JobseekerProfileResponse;
import com.example.gigwork.service.ProfileService;

@RestController
@RequestMapping("/api/jobseeker/profile")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    /**
     * 프로필 조회
     * GET /api/jobseeker/profile/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable("userId") Long userId) {
        try {
            JobseekerProfileResponse response = profileService.getProfile(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 상세 에러 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("프로필 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 프로필 수정
     * PUT /api/jobseeker/profile/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable("userId") Long userId,
            @RequestBody JobseekerProfileRequest request) {
        try {
            JobseekerProfileResponse response = profileService.updateProfile(userId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
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
