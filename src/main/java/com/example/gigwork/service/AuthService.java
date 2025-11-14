package com.example.gigwork.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gigwork.dto.AuthResponse;
import com.example.gigwork.dto.EmployerSignupRequest;
import com.example.gigwork.dto.JobseekerSignupRequest;
import com.example.gigwork.dto.LoginRequest;
import com.example.gigwork.entity.EmployerProfile;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.User;
import com.example.gigwork.enums.UserType;
import com.example.gigwork.repository.EmployerProfileRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.UserRepository;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;
    
    @Autowired
    private EmployerProfileRepository employerProfileRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // 1. 이메일로 사용자 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다."));
        
        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        
        // 3. 응답 생성
        return new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getUserType().name(),
            "로그인에 성공했습니다."
        );
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
        
        // 3. 사용자 삭제 (CASCADE로 프로필, 자격증, 경력, 공고 등 모두 삭제됨)
        userRepository.delete(user);
    }
}
