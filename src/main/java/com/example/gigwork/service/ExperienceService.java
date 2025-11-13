package com.example.gigwork.service;

import com.example.gigwork.dto.ExperienceRequest;
import com.example.gigwork.dto.ExperienceResponse;
import com.example.gigwork.entity.Experience;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.repository.ExperienceRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceService {
    
    @Autowired
    private ExperienceRepository experienceRepository;
    
    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;
    
    /**
     * 사용자의 모든 경력 조회
     */
    @Transactional(readOnly = true)
    public List<ExperienceResponse> getExperiences(Long userId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다: " + userId));
        
        List<Experience> experiences = experienceRepository.findByJobseekerId(profile.getId());
        
        return experiences.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 경력 추가
     */
    @Transactional
    public ExperienceResponse addExperience(Long userId, ExperienceRequest request) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다: " + userId));
        
        Experience experience = new Experience();
        experience.setJobseeker(profile);
        experience.setCompany(request.getCompany());
        experience.setPosition(request.getPosition());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setDescription(request.getDescription());
        
        Experience savedExperience = experienceRepository.save(experience);
        
        return convertToResponse(savedExperience);
    }
    
    /**
     * 경력 수정
     */
    @Transactional
    public ExperienceResponse updateExperience(Long experienceId, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("경력을 찾을 수 없습니다: " + experienceId));
        
        experience.setCompany(request.getCompany());
        experience.setPosition(request.getPosition());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setDescription(request.getDescription());
        
        Experience updatedExperience = experienceRepository.save(experience);
        
        return convertToResponse(updatedExperience);
    }
    
    /**
     * 경력 삭제
     */
    @Transactional
    public void deleteExperience(Long experienceId) {
        if (!experienceRepository.existsById(experienceId)) {
            throw new RuntimeException("경력을 찾을 수 없습니다: " + experienceId);
        }
        experienceRepository.deleteById(experienceId);
    }
    
    /**
     * Entity를 Response DTO로 변환
     */
    private ExperienceResponse convertToResponse(Experience experience) {
        return new ExperienceResponse(
            experience.getId(),
            experience.getCompany(),
            experience.getPosition(),
            experience.getStartDate(),
            experience.getEndDate(),
            experience.getDescription()
        );
    }
}
