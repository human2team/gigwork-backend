package com.example.gigwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // [모든 오리진 허용] - 현재 설정
        // allowedOriginPatterns를 사용하여 allowCredentials와 함께 사용 가능
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // [특정 오리진만 허용하는 경우]
        // 특정 도메인만 허용
        // configuration.setAllowedOriginPatterns(Arrays.asList("https://example.com", "https://www.example.com"));
        
        // 특정 도메인의 모든 서브도메인 허용
        // configuration.setAllowedOriginPatterns(Arrays.asList("https://*.example.com"));
  
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        // Preflight 요청 캐싱 시간 설정 (1시간)
        configuration.setMaxAge(3600L);
        // 노출할 헤더 설정
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}
