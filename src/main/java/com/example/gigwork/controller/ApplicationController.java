package com.example.gigwork.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gigwork.dto.ApplicationRequest;
import com.example.gigwork.dto.ApplicationResponse;
import com.example.gigwork.service.ApplicationService;

@RestController
@RequestMapping("/api/jobseeker/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 지원한 일자리 목록 조회
     * GET /api/jobseeker/applications/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getApplications(@PathVariable("userId") Long userId) {
        try {
            List<ApplicationResponse> applications = applicationService.getApplications(userId);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 일자리 지원
     * POST /api/jobseeker/applications/{userId}
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> applyJob(@PathVariable("userId") Long userId, @RequestBody ApplicationRequest request) {
        try {
            ApplicationResponse response = applicationService.applyJob(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 지원 취소
     * DELETE /api/jobseeker/applications/{userId}/{applicationId}
     */
    @DeleteMapping("/{userId}/{applicationId}")
    public ResponseEntity<?> cancelApplication(@PathVariable("userId") Long userId, @PathVariable("applicationId") Long applicationId) {
        try {
            applicationService.cancelApplication(userId, applicationId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "지원이 취소되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 지원 여부 확인
     * GET /api/jobseeker/applications/{userId}/check/{jobId}
     */
    @GetMapping("/{userId}/check/{jobId}")
    public ResponseEntity<?> isJobApplied(@PathVariable("userId") Long userId, @PathVariable("jobId") Long jobId) {
        try {
            boolean applied = applicationService.isJobApplied(userId, jobId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("applied", applied);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 사업자용 - 특정 공고의 지원자 목록 조회
     * GET /api/jobseeker/applications/job/{jobId}
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getApplicantsByJob(@PathVariable("jobId") Long jobId) {
        try {
            List<?> applicants = applicationService.getApplicantsByJob(jobId);
            return ResponseEntity.ok(applicants);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 사업자용 - 지원 상태 업데이트
     * PUT /api/jobseeker/applications/{applicationId}/status
     */
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable("applicationId") Long applicationId,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String status = statusUpdate.get("status");
            if (status == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("상태 값이 필요합니다."));
            }
            applicationService.updateApplicationStatus(applicationId, status);
            Map<String, String> response = new HashMap<>();
            response.put("message", "상태가 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
