package com.example.gigwork.repository;

import com.example.gigwork.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    
    /**
     * 이메일과 코드로 미사용 토큰 조회
     */
    Optional<EmailVerificationToken> findByEmailAndCodeAndUsedFalse(String email, String code);
    
    /**
     * 이메일로 모든 토큰 삭제 (재발송 시 기존 토큰 삭제)
     */
    void deleteByEmail(String email);
    
    /**
     * 만료된 토큰 삭제 (스케줄러용)
     */
    void deleteByExpiresAtBefore(LocalDateTime now);
}

