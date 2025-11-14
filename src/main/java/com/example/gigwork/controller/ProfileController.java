package com.example.gigwork.controller;

import com.example.gigwork.dto.JobseekerProfileRequest;
import com.example.gigwork.dto.JobseekerProfileResponse;
import com.example.gigwork.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobseeker/profile")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    /**
     * 프로필 조회
     * GET /api/jobseeker/profile/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        try {
            JobseekerProfileResponse response = profileService.getProfile(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 프로필 수정
     * PUT /api/jobseeker/profile/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
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
