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
