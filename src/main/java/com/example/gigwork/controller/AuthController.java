package com.example.gigwork.controller;

import com.example.gigwork.dto.AuthResponse;
import com.example.gigwork.dto.EmployerSignupRequest;
import com.example.gigwork.dto.JobseekerSignupRequest;
import com.example.gigwork.dto.LoginRequest;
import com.example.gigwork.dto.TokenRefreshRequest;
import com.example.gigwork.dto.TokenRefreshResponse;
import com.example.gigwork.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(request);
            
            // Access Token Cookie 설정 (1시간)
            Cookie accessCookie = new Cookie("accessToken", authResponse.getAccessToken());
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(60 * 60); // 1시간
            // accessCookie.setSameSite("Strict"); // Spring Boot 3.x에서는 setAttribute 사용
            response.addCookie(accessCookie);
            
            // Refresh Token Cookie 설정 (7일)
            Cookie refreshCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
            // refreshCookie.setSameSite("Strict");
            response.addCookie(refreshCookie);
            
            // Response Body에서는 토큰 제거
            authResponse.setAccessToken(null);
            authResponse.setRefreshToken(null);
            
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Access Token 갱신 API
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Cookie에서 Refresh Token 추출
            String refreshToken = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }
            
            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Refresh Token이 없습니다"));
            }
            
            TokenRefreshResponse tokenResponse = authService.refreshToken(refreshToken);
            
            // 새 Access Token Cookie 설정
            Cookie accessCookie = new Cookie("accessToken", tokenResponse.getAccessToken());
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(60 * 60); // 1시간
            response.addCookie(accessCookie);
            
            // 새 Refresh Token Cookie 설정
            Cookie refreshCookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
            response.addCookie(refreshCookie);
            
            // Response Body에서는 토큰 제거
            tokenResponse.setAccessToken(null);
            tokenResponse.setRefreshToken(null);
            
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 로그아웃 API
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Cookie에서 사용자 이메일 추출 (accessToken에서)
            String accessToken = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("accessToken".equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        break;
                    }
                }
            }
            
            if (accessToken != null) {
                // JwtTokenProvider를 통해 email 추출 (AuthService에서 처리)
                authService.logout(accessToken);
            }
            
            // Access Token Cookie 삭제
            Cookie accessCookie = new Cookie("accessToken", null);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(0);
            response.addCookie(accessCookie);
            
            // Refresh Token Cookie 삭제
            Cookie refreshCookie = new Cookie("refreshToken", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/api/auth");
            refreshCookie.setMaxAge(0);
            response.addCookie(refreshCookie);
            
            return ResponseEntity.ok(new SuccessResponse("로그아웃이 완료되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
