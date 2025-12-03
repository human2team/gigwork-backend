package com.example.gigwork.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * .env 파일을 로드하여 시스템 환경변수로 설정하는 Configuration 클래스
 */
@Configuration
public class DotenvConfig {
    
    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // .env 파일이 없어도 에러 발생하지 않음
                .load();
        
        // .env 파일의 값들을 시스템 환경변수로 설정
        // System.getProperty로 이미 설정된 값이 있으면 덮어쓰지 않음
        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            if (System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        });
    }
}

