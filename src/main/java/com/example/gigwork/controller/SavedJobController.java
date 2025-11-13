package com.example.gigwork.controller;

import com.example.gigwork.dto.JobResponse;
import com.example.gigwork.service.SavedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobseeker/saved-jobs")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SavedJobController {

    @Autowired
    private SavedJobService savedJobService;

    /**
     * 저장된 일자리 목록 조회
     * GET /api/jobseeker/saved-jobs/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getSavedJobs(@PathVariable Long userId) {
        try {
            List<JobResponse> jobs = savedJobService.getSavedJobs(userId);
            return ResponseEntity.ok(jobs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 일자리 저장
     * POST /api/jobseeker/saved-jobs/{userId}/{jobId}
     */
    @PostMapping("/{userId}/{jobId}")
    public ResponseEntity<?> saveJob(@PathVariable Long userId, @PathVariable Long jobId) {
        try {
            savedJobService.saveJob(userId, jobId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "일자리가 저장되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 일자리 저장 해제
     * DELETE /api/jobseeker/saved-jobs/{userId}/{jobId}
     */
    @DeleteMapping("/{userId}/{jobId}")
    public ResponseEntity<?> unsaveJob(@PathVariable Long userId, @PathVariable Long jobId) {
        try {
            savedJobService.unsaveJob(userId, jobId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "저장이 해제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 일자리 저장 여부 확인
     * GET /api/jobseeker/saved-jobs/{userId}/check/{jobId}
     */
    @GetMapping("/{userId}/check/{jobId}")
    public ResponseEntity<?> isJobSaved(@PathVariable Long userId, @PathVariable Long jobId) {
        try {
            boolean saved = savedJobService.isJobSaved(userId, jobId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("saved", saved);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
