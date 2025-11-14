package com.example.gigwork.controller;

import com.example.gigwork.dto.AuthResponse;
import com.example.gigwork.dto.EmployerSignupRequest;
import com.example.gigwork.dto.JobseekerSignupRequest;
import com.example.gigwork.dto.LoginRequest;
import com.example.gigwork.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 구직자 회원가입 API
     * POST /api/auth/signup/jobseeker
     */
    @PostMapping("/signup/jobseeker")
    public ResponseEntity<?> signupJobseeker(@RequestBody JobseekerSignupRequest request) {
        try {
            AuthResponse response = authService.registerJobseeker(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // 이메일 중복 등의 에러 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 사업자 회원가입 API
     * POST /api/auth/signup/employer
     */
    @PostMapping("/signup/employer")
    public ResponseEntity<?> signupEmployer(@RequestBody EmployerSignupRequest request) {
        try {
            AuthResponse response = authService.registerEmployer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // 이메일/사업자등록번호 중복 등의 에러 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 로그인 API
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 비밀번호 변경 API
     * PUT /api/auth/change-password/{userId}
     */
    @PutMapping("/change-password/{userId}")
    public ResponseEntity<?> changePassword(
            @PathVariable("userId") Long userId,
            @RequestBody ChangePasswordRequest request) {
        try {
            authService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok(new SuccessResponse("비밀번호가 성공적으로 변경되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 계정 탈퇴 API
     * DELETE /api/auth/account/{userId}
     */
    @DeleteMapping("/account/{userId}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable("userId") Long userId,
            @RequestBody DeleteAccountRequest request) {
        try {
            authService.deleteAccount(userId, request.getPassword());
            return ResponseEntity.ok(new SuccessResponse("계정이 성공적으로 탈퇴되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 비밀번호 변경 요청 DTO
     */
    static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;
        
        public String getCurrentPassword() {
            return currentPassword;
        }
        
        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
    
    /**
     * 계정 탈퇴 요청 DTO
     */
    static class DeleteAccountRequest {
        private String password;
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    /**
     * 성공 응답용 클래스
     */
    static class SuccessResponse {
        private String message;
        
        public SuccessResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
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
