package com.example.gigwork.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
}
