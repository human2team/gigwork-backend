package com.example.gigwork.controller;

import com.example.gigwork.dto.LicenseRequest;
import com.example.gigwork.dto.LicenseResponse;
import com.example.gigwork.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobseeker/licenses")
public class LicenseController {
    
    @Autowired
    private LicenseService licenseService;
    
    /**
     * 사용자의 모든 자격증 조회
     * GET /api/jobseeker/licenses/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<LicenseResponse>> getLicenses(@PathVariable Long userId) {
        try {
            List<LicenseResponse> licenses = licenseService.getLicenses(userId);
            return ResponseEntity.ok(licenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 자격증 추가
     * POST /api/jobseeker/licenses/{userId}
     */
    @PostMapping("/{userId}")
    public ResponseEntity<LicenseResponse> addLicense(
            @PathVariable Long userId,
            @RequestBody LicenseRequest request) {
        try {
            LicenseResponse license = licenseService.addLicense(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(license);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 자격증 수정
     * PUT /api/jobseeker/licenses/{licenseId}
     */
    @PutMapping("/{licenseId}")
    public ResponseEntity<LicenseResponse> updateLicense(
            @PathVariable Long licenseId,
            @RequestBody LicenseRequest request) {
        try {
            LicenseResponse license = licenseService.updateLicense(licenseId, request);
            return ResponseEntity.ok(license);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 자격증 삭제
     * DELETE /api/jobseeker/licenses/{licenseId}
     */
    @DeleteMapping("/{licenseId}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long licenseId) {
        try {
            licenseService.deleteLicense(licenseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
