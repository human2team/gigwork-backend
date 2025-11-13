package com.example.gigwork.service;

import com.example.gigwork.dto.LicenseRequest;
import com.example.gigwork.dto.LicenseResponse;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.License;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseService {
    
    @Autowired
    private LicenseRepository licenseRepository;
    
    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;
    
    /**
     * 사용자의 모든 자격증 조회
     */
    @Transactional(readOnly = true)
    public List<LicenseResponse> getLicenses(Long userId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다: " + userId));
        
        List<License> licenses = licenseRepository.findByJobseekerId(profile.getId());
        
        return licenses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 자격증 추가
     */
    @Transactional
    public LicenseResponse addLicense(Long userId, LicenseRequest request) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다: " + userId));
        
        License license = new License();
        license.setJobseeker(profile);
        license.setName(request.getName());
        license.setIssueDate(request.getIssueDate());
        license.setExpiryDate(request.getExpiryDate());
        
        License savedLicense = licenseRepository.save(license);
        
        return convertToResponse(savedLicense);
    }
    
    /**
     * 자격증 수정
     */
    @Transactional
    public LicenseResponse updateLicense(Long licenseId, LicenseRequest request) {
        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new RuntimeException("자격증을 찾을 수 없습니다: " + licenseId));
        
        license.setName(request.getName());
        license.setIssueDate(request.getIssueDate());
        license.setExpiryDate(request.getExpiryDate());
        
        License updatedLicense = licenseRepository.save(license);
        
        return convertToResponse(updatedLicense);
    }
    
    /**
     * 자격증 삭제
     */
    @Transactional
    public void deleteLicense(Long licenseId) {
        if (!licenseRepository.existsById(licenseId)) {
            throw new RuntimeException("자격증을 찾을 수 없습니다: " + licenseId);
        }
        licenseRepository.deleteById(licenseId);
    }
    
    /**
     * Entity를 Response DTO로 변환
     */
    private LicenseResponse convertToResponse(License license) {
        return new LicenseResponse(
            license.getId(),
            license.getName(),
            license.getIssueDate(),
            license.getExpiryDate()
        );
    }
}
