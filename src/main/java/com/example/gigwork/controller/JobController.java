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
import org.springframework.web.bind.annotation.RestController;

import com.example.gigwork.dto.JobCreateRequest;
import com.example.gigwork.dto.JobDetailResponse;
import com.example.gigwork.service.JobService;

import jakarta.servlet.http.HttpSession;

@RestController
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    /**
     * 모든 활성 공고 조회 (구직자용)
     * GET /api/jobs/active
     */
    @GetMapping("/api/jobs/active")
    public ResponseEntity<?> getAllActiveJobs() {
        try {
            List<JobDetailResponse> jobs = jobService.getActiveJobs();
            return ResponseEntity.ok(jobs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 일자리 공고 등록
     * POST /api/employer/jobs/{employerId}
     */
    @PostMapping("/api/employer/jobs/{employerId}")
    public ResponseEntity<?> createJob(@PathVariable("employerId") Long employerId, @RequestBody JobCreateRequest request) {
        try {
            JobDetailResponse response = jobService.createJob(employerId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 일자리 공고 상세 조회 (조회수 증가 없음)
     * GET /api/employer/jobs/detail/{jobId}
     */
    @GetMapping("/api/employer/jobs/detail/{jobId}")
    public ResponseEntity<?> getJob(@PathVariable("jobId") Long jobId) {
        try {
            JobDetailResponse response = jobService.getJobWithoutIncrement(jobId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 일자리 공고 상세 조회 (구직자용 - 조회수 증가)
     * GET /api/jobs/detail/{jobId}
     */
    @GetMapping("/api/jobs/detail/{jobId}")
    public ResponseEntity<?> getJobForJobseeker(@PathVariable("jobId") Long jobId, HttpSession session) {
        try {
            JobDetailResponse response = jobService.getJob(jobId, session);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 사업자의 일자리 공고 목록 조회
     * GET /api/employer/jobs/{employerId}
     */
    @GetMapping("/api/employer/jobs/{employerId}")
    public ResponseEntity<?> getEmployerJobs(@PathVariable("employerId") Long employerId) {
        try {
            List<JobDetailResponse> jobs = jobService.getEmployerJobs(employerId);
            return ResponseEntity.ok(jobs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 일자리 공고 수정
     * PUT /api/employer/jobs/{jobId}
     */
    @PutMapping("/api/employer/jobs/{jobId}")
    public ResponseEntity<?> updateJob(@PathVariable("jobId") Long jobId, @RequestBody JobCreateRequest request) {
        try {
            JobDetailResponse response = jobService.updateJob(jobId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 일자리 공고 삭제
     * DELETE /api/employer/jobs/{jobId}
     */
    @DeleteMapping("/api/employer/jobs/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable("jobId") Long jobId) {
        try {
            jobService.deleteJob(jobId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "공고가 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 일자리 공고 상태 변경
     * PATCH /api/employer/jobs/{jobId}/status
     */
    @PutMapping("/api/employer/jobs/{jobId}/status")
    public ResponseEntity<?> updateJobStatus(@PathVariable("jobId") Long jobId, @RequestBody Map<String, String> statusRequest) {
        try {
            String status = statusRequest.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("상태 값이 필요합니다."));
            }
            
            JobDetailResponse response = jobService.updateJobStatus(jobId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 에러 응답 생성
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        return error;
    }
}
