package com.example.gigwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 이메일 발송 서비스
 * Gmail SMTP를 사용하여 이메일을 발송합니다.
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.mail.from}")
    private String fromEmail;
    
    @Value("${app.mail.from-name:GigWork}")
    private String fromName;
    
    /**
     * 인증 코드 이메일 발송
     * @param toEmail 수신자 이메일 주소
     * @param code 6자리 인증 코드
     */
    public void sendVerificationCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[GigWork] 이메일 인증 코드");
            message.setText(
                "안녕하세요, " + fromName + "입니다.\n\n" +
                "이메일 인증 코드는 다음과 같습니다:\n\n" +
                "인증 코드: " + code + "\n\n" +
                "이 코드는 10분간 유효합니다.\n" +
                "본인이 요청한 것이 아니라면 무시하셔도 됩니다.\n\n" +
                "감사합니다.\n" +
                fromName + " 팀"
            );
            
            mailSender.send(message);
            logger.info("인증 코드 이메일 발송 성공: {}", toEmail);
        } catch (Exception e) {
            logger.error("인증 코드 이메일 발송 실패: {}", toEmail, e);
            throw new RuntimeException("이메일 발송에 실패했습니다: " + e.getMessage(), e);
        }
    }
}

