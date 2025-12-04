package com.example.gigwork.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gigwork.dto.AuthResponse;
import com.example.gigwork.dto.EmployerSignupRequest;
import com.example.gigwork.dto.JobseekerSignupRequest;
import com.example.gigwork.dto.LoginRequest;
import com.example.gigwork.dto.TokenRefreshResponse;
import com.example.gigwork.entity.EmployerProfile;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.RefreshToken;
import com.example.gigwork.entity.User;
import com.example.gigwork.enums.UserType;
import com.example.gigwork.repository.EmployerProfileRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.RefreshTokenRepository;
import com.example.gigwork.repository.UserRepository;
import com.example.gigwork.security.jwt.JwtTokenProvider;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    // 사용자별 refresh lock을 위한 동기화 객체 (메모리 최적화)
    private final java.util.concurrent.ConcurrentHashMap<String, Object> refreshLocks = new java.util.concurrent.ConcurrentHashMap<>();
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;
    
    @Autowired
    private EmployerProfileRepository employerProfileRepository;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Transactional
    public AuthResponse registerJobseeker(JobseekerSignupRequest request) {
        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        // 2. User 엔티티 생성 (비밀번호 암호화)
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화
        user.setUserType(UserType.JOBSEEKER);
        
        // User 저장
        User savedUser = userRepository.save(user);
        
        // 3. JobseekerProfile 엔티티 생성
        JobseekerProfile profile = new JobseekerProfile();
        profile.setUser(savedUser);
        profile.setName(request.getName());
        profile.setPhone(request.getPhone());
        
        // 생년월일 변환 (String -> LocalDate)
        if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) {
            profile.setBirthDate(LocalDate.parse(request.getBirthDate()));
        }
        
        // Profile 저장
        jobseekerProfileRepository.save(profile);
        
        // 4. 응답 생성
        return new AuthResponse(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getUserType().name(),
            "회원가입이 완료되었습니다."
        );
    }
    
    /**
     * 사업자 회원가입
     */
    @Transactional
    public AuthResponse registerEmployer(EmployerSignupRequest request) {
        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        // 2. 사업자등록번호 중복 체크
        if (request.getBusinessNumber() != null && 
            employerProfileRepository.existsByBusinessNumber(request.getBusinessNumber())) {
            throw new RuntimeException("이미 등록된 사업자등록번호입니다: " + request.getBusinessNumber());
        }
        
        // 3. User 엔티티 생성 (비밀번호 암호화)
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserType(UserType.EMPLOYER);
        
        // User 저장
        User savedUser = userRepository.save(user);
        
        // 4. EmployerProfile 엔티티 생성
        EmployerProfile profile = new EmployerProfile();
        profile.setUser(savedUser);
        profile.setCompanyName(request.getCompanyName());
        profile.setBusinessNumber(request.getBusinessNumber());
        profile.setRepresentativeName(request.getRepresentativeName());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        
        // Profile 저장
        employerProfileRepository.save(profile);
        
        // 5. 응답 생성
        return new AuthResponse(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getUserType().name(),
            "사업자 회원가입이 완료되었습니다."
        );
    }
    
    /**
     * 로그인
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // 1. 이메일로 사용자 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다."));
        
        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        
        // 3. JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(
            user.getEmail(), 
            user.getUserType().name()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
        
        // 4. Refresh Token을 User 엔티티에 저장
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(jwtTokenProvider.getRefreshTokenExpiry());
        userRepository.save(user);
        
        // 5. 응답 생성
        AuthResponse response = new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getUserType().name(),
            "로그인에 성공했습니다."
        );
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        
        return response;
    }
    
    /**
     * Access Token 갱신
     */
    @Transactional
    public TokenRefreshResponse refreshToken(String refreshToken) {
        logger.info("AuthService.refreshToken: called with refreshToken {}", refreshToken == null ? "<null>" : (refreshToken.length() <= 8 ? refreshToken : refreshToken.substring(0,8) + "..."));
        
        // 1. Refresh Token 검증 (서명/만료 확인)
        logger.info("AuthService.refreshToken: validating token...");
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            logger.warn("AuthService.refreshToken: refresh token validation failed");
            throw new RuntimeException("유효하지 않은 Refresh Token입니다");
        }
        logger.info("AuthService.refreshToken: token validation passed");
        
        // 2. Token에서 email 추출
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        logger.info("AuthService.refreshToken: extracted email: {}", email);
        
        // 사용자별 동기화 - 동일 사용자의 동시 refresh 방지
        Object lock = refreshLocks.computeIfAbsent(email, k -> new Object());
        
        synchronized (lock) {
            logger.info("AuthService.refreshToken: acquired lock for user {}", email);
            
            // 3. DB에서 사용자 조회 및 Refresh Token 확인
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
            
            logger.info("AuthService.refreshToken: user found, DB token: {}", 
                user.getRefreshToken() == null ? "<null>" : (user.getRefreshToken().length() <= 8 ? user.getRefreshToken() : user.getRefreshToken().substring(0,8) + "..."));
            
            // 4. DB에 저장된 refresh token과 비교
            if (!refreshToken.equals(user.getRefreshToken())) {
                logger.warn("AuthService.refreshToken: token mismatch - received vs DB (another thread may have refreshed)");
                throw new RuntimeException("유효하지 않은 Refresh Token입니다");
            }
            logger.info("AuthService.refreshToken: token match confirmed");
            
            // 5. 만료 확인 (isAfter를 사용하여 명확하게 체크)
            LocalDateTime now = LocalDateTime.now();
            if (user.getRefreshTokenExpiry() == null || !user.getRefreshTokenExpiry().isAfter(now)) {
                logger.info("AuthService.refreshToken: refresh token expired - expiry: {}, now: {}", 
                    user.getRefreshTokenExpiry(), now);
                user.setRefreshToken(null);
                user.setRefreshTokenExpiry(null);
                userRepository.save(user);
                throw new RuntimeException("Refresh Token이 만료되었습니다");
            }
            
            // 6. 새 Access Token 생성
            String newAccessToken = jwtTokenProvider.createAccessToken(
                user.getEmail(),
                user.getUserType().name()
            );
            
            // 7. 새 Refresh Token 생성 (Rotation 보안 강화)
            String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            user.setRefreshToken(newRefreshToken);
            user.setRefreshTokenExpiry(jwtTokenProvider.getRefreshTokenExpiry());
            userRepository.save(user);
            
            logger.info("AuthService.refreshToken: issued new tokens successfully");
            
            return new TokenRefreshResponse(
                newAccessToken,
                newRefreshToken,
                "토큰이 갱신되었습니다."
            );
        }
    }
    
    /**
     * 로그아웃
     */
    @Transactional
    public void logout(String accessToken) {
        // Access Token에서 email 추출
        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        // Refresh Token 무효화
        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);
        userRepository.save(user);
    }
    
    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        // 1. 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 2. 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }
        
        // 3. 새 비밀번호 유효성 검사
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("새 비밀번호는 최소 6자 이상이어야 합니다.");
        }
        
        // 4. 비밀번호 변경
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    /**
     * 계정 탈퇴
     * User 삭제 시 CASCADE 설정으로 인해 Profile, License, Experience 등도 자동 삭제됨
     */
    @Transactional
    public void deleteAccount(Long userId, String password) {
        // 1. 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 2. 비밀번호 확인 (본인 인증)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        // 3. Refresh Token 무효화
        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);
        
        // 4. 연관 Refresh Token 선삭제 (FK 제약 위반 방지)
        refreshTokenRepository.deleteByUser(user);

        // 5. 사용자 삭제 (CASCADE로 프로필, 자격증, 경력, 공고 등 모두 삭제됨)
        userRepository.delete(user);
    }
}
