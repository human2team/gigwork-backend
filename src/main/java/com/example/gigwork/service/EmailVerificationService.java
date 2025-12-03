package com.example.gigwork.service;

import com.example.gigwork.entity.EmailVerificationToken;
import com.example.gigwork.repository.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * 이메일 인증 서비스
 * 인증 코드 생성, 발송, 검증을 담당합니다.
 */
@Service
public class EmailVerificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);
    
    @Autowired
    private EmailVerificationTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * 인증 코드 생성 및 이메일 발송
     * @param email 수신자 이메일 주소
     * @return 생성된 인증 코드 (테스트용, 프로덕션에서는 반환하지 않아도 됨)
     */
    @Transactional
    public String sendVerificationCode(String email) {
        // 기존 미사용 토큰 삭제 (재발송 시)
        tokenRepository.deleteByEmail(email);
        
        // 6자리 랜덤 코드 생성
        String code = generateVerificationCode();
        
        // 토큰 저장
        EmailVerificationToken token = new EmailVerificationToken();
        token.setEmail(email);
        token.setCode(code);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10)); // 10분 유효
        token.setUsed(false);
        tokenRepository.save(token);
        
        // 이메일 발송
        emailService.sendVerificationCode(email, code);
        
        logger.info("인증 코드 발송 완료: email={}", email);
        return code; // 테스트용 (프로덕션에서는 제거 가능)
    }
    
    /**
     * 인증 코드 검증
     * @param email 이메일 주소
     * @param code 인증 코드
     * @return 검증 성공 여부
     */
    @Transactional
    public boolean verifyCode(String email, String code) {
        Optional<EmailVerificationToken> tokenOpt = 
            tokenRepository.findByEmailAndCodeAndUsedFalse(email, code);
        
        if (tokenOpt.isEmpty()) {
            logger.warn("인증 코드 불일치 또는 이미 사용됨: email={}", email);
            return false;
        }
        
        EmailVerificationToken token = tokenOpt.get();
        
        // 만료 확인
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            logger.warn("인증 코드 만료: email={}, expiresAt={}", email, token.getExpiresAt());
            tokenRepository.delete(token);
            return false;
        }
        
        // 사용 처리
        token.setUsed(true);
        tokenRepository.save(token);
        
        logger.info("인증 코드 검증 성공: email={}", email);
        return true;
    }
    
    /**
     * 6자리 랜덤 인증 코드 생성
     * @return 6자리 숫자 코드 (100000 ~ 999999)
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }
    
    /**
     * 만료된 토큰 정리 (매일 자정 실행)
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = tokenRepository.findAll().stream()
            .filter(token -> token.getExpiresAt().isBefore(now))
            .mapToInt(token -> {
                tokenRepository.delete(token);
                return 1;
            })
            .sum();
        
        logger.info("만료된 인증 토큰 정리 완료: {}건 삭제", deletedCount);
    }
}

