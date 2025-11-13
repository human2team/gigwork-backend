package com.example.gigwork.service;

import com.example.gigwork.dto.JobseekerProfileRequest;
import com.example.gigwork.dto.JobseekerProfileResponse;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.User;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;
    
    /**
     * 사용자 ID로 프로필 조회
     */
    @Transactional
    public JobseekerProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        
        // 프로필이 없으면 기본 프로필 생성
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    JobseekerProfile newProfile = new JobseekerProfile();
                    newProfile.setUser(user);
                    newProfile.setName(""); // 기본값
                    return jobseekerProfileRepository.save(newProfile);
                });
        
        return convertToResponse(user, profile);
    }
    
    /**
     * 프로필 업데이트
     */
    @Transactional
    public JobseekerProfileResponse updateProfile(Long userId, JobseekerProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        
        // 프로필이 없으면 새로 생성
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    JobseekerProfile newProfile = new JobseekerProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });
        
        // 프로필 정보 업데이트
        profile.setName(request.getName());
        profile.setPhone(request.getPhone());
        profile.setBirthDate(request.getBirthDate());
        profile.setGender(request.getGender());
        profile.setAddress(request.getAddress());
        profile.setEducation(request.getEducation());
        
        // 희망 근무 조건 업데이트
        profile.setPreferredRegion(request.getPreferredRegion());
        profile.setPreferredDistrict(request.getPreferredDistrict());
        profile.setPreferredDong(request.getPreferredDong());
        profile.setWorkDuration(request.getWorkDuration());
        profile.setWorkDays(request.getWorkDays());
        profile.setWorkTime(request.getWorkTime());
        
        // 신체 속성 업데이트
        profile.setMuscleStrength(request.getMuscleStrength());
        profile.setHeight(request.getHeight());
        profile.setWeight(request.getWeight());
        
        // 기타 정보 업데이트
        profile.setStrengths(request.getStrengths());
        profile.setMbti(request.getMbti());
        profile.setIntroduction(request.getIntroduction());
        
        JobseekerProfile updatedProfile = jobseekerProfileRepository.save(profile);
        
        return convertToResponse(user, updatedProfile);
    }
    
    /**
     * Entity를 Response DTO로 변환
     */
    private JobseekerProfileResponse convertToResponse(User user, JobseekerProfile profile) {
        return new JobseekerProfileResponse(
            profile.getId(),
            profile.getName(),
            user.getEmail(),
            profile.getPhone(),
            profile.getBirthDate(),
            profile.getGender(),
            profile.getAddress(),
            profile.getEducation(),
            profile.getPreferredRegion(),
            profile.getPreferredDistrict(),
            profile.getPreferredDong(),
            profile.getWorkDuration(),
            profile.getWorkDays(),
            profile.getWorkTime(),
            profile.getMuscleStrength(),
            profile.getHeight(),
            profile.getWeight(),
            profile.getStrengths(),
            profile.getMbti(),
            profile.getIntroduction()
        );
    }
}
