package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Long userId;
    private String email;
    private String userType;
    private String message;
    private String accessToken;
    private String refreshToken;
    
    // 기존 생성자 호환성을 위한 생성자
    public AuthResponse(Long userId, String email, String userType, String message) {
        this.userId = userId;
        this.email = email;
        this.userType = userType;
        this.message = message;
    }
}
