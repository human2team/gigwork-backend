package com.example.gigwork.controller;

import com.example.gigwork.repository.UserRepository;
import com.example.gigwork.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 이메일 인증 컨트롤러
 */
@RestController
@RequestMapping("/api/auth/email")
public class EmailVerificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationController.class);
    
    @Autowired
    private EmailVerificationService verificationService;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 인증 코드 발송
     * POST /api/auth/email/send
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            
            if (email == null || email.isEmpty() || !email.contains("@")) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("유효한 이메일 주소를 입력해주세요."));
            }
            
            verificationService.sendVerificationCode(email);
            return ResponseEntity.ok(createSuccessResponse("인증 코드가 발송되었습니다."));
        } catch (Exception e) {
            logger.error("인증 코드 발송 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("인증 코드 발송에 실패했습니다: " + e.getMessage()));
        }
    }
    
    /**
     * 이메일 중복 체크
     * GET /api/auth/email/check?email={email}
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        try {
            if (email == null || email.isEmpty() || !email.contains("@")) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("유효한 이메일 주소를 입력해주세요."));
            }
            
            boolean exists = userRepository.existsByEmail(email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("message", exists ? "이미 존재하는 이메일입니다." : "사용 가능한 이메일입니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("이메일 중복 체크 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("이메일 중복 체크에 실패했습니다: " + e.getMessage()));
        }
    }
    
    /**
     * 인증 코드 검증
     * POST /api/auth/email/verify
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String code = request.get("code");
            
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("이메일 주소를 입력해주세요."));
            }
            
            if (code == null || code.isEmpty() || code.length() != 6) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("6자리 인증 코드를 입력해주세요."));
            }
            
            boolean verified = verificationService.verifyCode(email, code);
            
            if (verified) {
                return ResponseEntity.ok(createSuccessResponse("이메일 인증이 완료되었습니다."));
            } else {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("인증 코드가 일치하지 않거나 만료되었습니다."));
            }
        } catch (Exception e) {
            logger.error("인증 코드 검증 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("인증 코드 검증에 실패했습니다: " + e.getMessage()));
        }
    }
    
    /**
     * 성공 응답 생성
     */
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", message);
        return response;
    }
    
    /**
     * 에러 응답 생성
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("success", "false");
        response.put("message", message);
        return response;
    }
}

