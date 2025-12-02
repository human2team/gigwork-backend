package com.example.gigwork.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.gigwork.dto.JobCreateRequest;
import com.example.gigwork.dto.JobDetailResponse;
import com.example.gigwork.entity.EmployerProfile;
import com.example.gigwork.entity.Job;
import com.example.gigwork.enums.JobStatus;
import com.example.gigwork.repository.EmployerProfileRepository;
import com.example.gigwork.repository.JobRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JobService {
    
    private static final Logger log = LoggerFactory.getLogger(JobService.class);
    private static final String FASTAPI_UPDATE_URL = "http://localhost:8000/update/";
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EmployerProfileRepository employerProfileRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    //임베딩 및 FastAPI 호출 공통 메서드
    private void updateJobEmbeddingAfterCommit(Long jobId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("job_id", jobId);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                    log.info("[afterCommit] FastAPI /update/ 호출: job_id={}", jobId);
                    Map<String, Object> fastApiResponse = restTemplate.postForObject(
                        FASTAPI_UPDATE_URL,
                        entity,
                        Map.class
                    );
                    log.info("[afterCommit] 📥 FastAPI 응답 전체: {}", fastApiResponse);
                } catch (RestClientException e) {
                    log.error("[afterCommit] ❌ FastAPI 호출 실패: {}", e.getMessage(), e);
                }
            }
        });
    }
    
    /**
     * 일자리 공고 등록
     */
    @Transactional
    public JobDetailResponse createJob(Long employerId, JobCreateRequest request) {
        // 사업자 프로필 조회
        EmployerProfile employer = employerProfileRepository.findByUserId(employerId)
                .orElseThrow(() -> new RuntimeException("사업자 프로필을 찾을 수 없습니다."));
        
        // Job 엔티티 생성
        Job job = new Job();
        job.setEmployer(employer);
        job.setTitle(request.getTitle());
        job.setCategory(request.getCategory());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setStatus(JobStatus.ACTIVE);
        
        // JSON 변환하여 저장
        try {
            if (request.getQualifications() != null && !request.getQualifications().isEmpty()) {
                job.setQualifications(objectMapper.writeValueAsString(request.getQualifications()));
            }
            if (request.getRequirements() != null && !request.getRequirements().isEmpty()) {
                job.setRequirements(objectMapper.writeValueAsString(request.getRequirements()));
            }
            if (request.getWorkingDays() != null && !request.getWorkingDays().isEmpty()) {
                job.setWorkDays(objectMapper.writeValueAsString(request.getWorkingDays()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 오류: " + e.getMessage());
        }
        
        job.setOtherRequirement(request.getOtherRequirement());
        job.setStartTime(request.getStartTime());
        job.setEndTime(request.getEndTime());
        job.setSalary(request.getSalary());
        job.setSalaryType(request.getSalaryType());
        job.setDeadline(request.getDeadline());
        job.setGender(request.getGender());
        job.setAge(request.getAge());
        job.setEducation(request.getEducation());
        
        // 근무 시간 형식화 (예: "09:00 - 18:00")
        if (request.getStartTime() != null && request.getEndTime() != null) {
            job.setWorkHours(request.getStartTime() + " - " + request.getEndTime());
        }
        
        // 저장
        Job savedJob = jobRepository.save(job);
        jobRepository.flush();

        Long jobId = savedJob.getId();

        // 응답 변환
        updateJobEmbeddingAfterCommit(jobId);
        return convertToDetailResponse(savedJob);
    }
    
    /**
     * 일자리 공고 상세 조회 (조회수 증가 없음 - 사업자용)
     */
    @Transactional(readOnly = true)
    public JobDetailResponse getJobWithoutIncrement(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("일자리 공고를 찾을 수 없습니다."));
        return convertToDetailResponse(job);
    }
    
    /**
     * 일자리 공고 상세 조회 (조회수 증가)
     */
    @Transactional
    public JobDetailResponse getJob(Long jobId, jakarta.servlet.http.HttpSession session) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("일자리 공고를 찾을 수 없습니다."));
        
        // 세션에서 조회한 공고 목록 가져오기
        @SuppressWarnings("unchecked")
        java.util.Set<Long> viewedJobs = (java.util.Set<Long>) session.getAttribute("viewedJobs");
        if (viewedJobs == null) {
            viewedJobs = new java.util.HashSet<>();
        }
        
        // 이 공고를 처음 조회하는 경우에만 조회수 증가
        if (!viewedJobs.contains(jobId)) {
            Integer currentViews = job.getViews();
            if (currentViews == null) {
                currentViews = 0;
            }
            job.setViews(currentViews + 1);
            
            // 세션에 조회 기록 추가
            viewedJobs.add(jobId);
            session.setAttribute("viewedJobs", viewedJobs);
            
            // 변경사항 저장
            job = jobRepository.save(job);
        }
        
        return convertToDetailResponse(job);
    }
    
    /**
     * 사업자의 일자리 공고 목록 조회
     */
    @Transactional(readOnly = true)
    public List<JobDetailResponse> getEmployerJobs(Long employerId) {
        // employerId는 User의 ID이므로 findByEmployerUserId 사용
        List<Job> jobs = jobRepository.findByEmployerUserId(employerId);
        return jobs.stream()
                .map(this::convertToDetailResponse)
                .toList();
    }
    
    /**
     * 일자리 공고 수정
     */
    @Transactional
    public JobDetailResponse updateJob(Long jobId, JobCreateRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("일자리 공고를 찾을 수 없습니다."));
        
        // 기본 정보 업데이트
        job.setTitle(request.getTitle());
        job.setCategory(request.getCategory());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setAddressDetail(request.getAddressDetail());
        job.setDescription(request.getDescription());
        
        // JSON 변환하여 저장
        try {
            if (request.getQualifications() != null && !request.getQualifications().isEmpty()) {
                job.setQualifications(objectMapper.writeValueAsString(request.getQualifications()));
            }
            if (request.getRequirements() != null && !request.getRequirements().isEmpty()) {
                job.setRequirements(objectMapper.writeValueAsString(request.getRequirements()));
            }
            if (request.getWorkingDays() != null && !request.getWorkingDays().isEmpty()) {
                job.setWorkDays(objectMapper.writeValueAsString(request.getWorkingDays()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 오류: " + e.getMessage());
        }
        
        job.setOtherRequirement(request.getOtherRequirement());
        job.setStartTime(request.getStartTime());
        job.setEndTime(request.getEndTime());
        job.setSalary(request.getSalary());
        job.setSalaryType(request.getSalaryType());
        job.setDeadline(request.getDeadline());
        job.setGender(request.getGender());
        job.setAge(request.getAge());
        job.setEducation(request.getEducation());
        
        // 근무 시간 형식화
        if (request.getStartTime() != null && request.getEndTime() != null) {
            job.setWorkHours(request.getStartTime() + " - " + request.getEndTime());
        }
        
        // 저장
        Job updatedJob = jobRepository.save(job);
        jobRepository.flush();
        updateJobEmbeddingAfterCommit(jobId);
        // 응답 변환
        return convertToDetailResponse(updatedJob);
    }
    
    /**
     * 일자리 공고 삭제
     */
    @Transactional
    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("일자리 공고를 찾을 수 없습니다."));
        jobRepository.delete(job);
    }
    
    /**
     * 일자리 공고 상태 변경
     */
    @Transactional
    public JobDetailResponse updateJobStatus(Long jobId, String status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("일자리 공고를 찾을 수 없습니다."));
        
        JobStatus newStatus;
        try {
            newStatus = JobStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 상태 값입니다: " + status);
        }
        
        job.setStatus(newStatus);
        Job updatedJob = jobRepository.save(job);
        
        return convertToDetailResponse(updatedJob);
    }
    
    /**
     * 모든 활성 공고 조회 (구직자용)
     */
    @Transactional(readOnly = true)
    public List<JobDetailResponse> getActiveJobs() {
        List<Job> jobs = jobRepository.findByStatus(JobStatus.ACTIVE);
        return jobs.stream()
                .map(this::convertToDetailResponse)
                .toList();
    }
    
    /**
     * Job 엔티티를 JobDetailResponse로 변환
     */
    private JobDetailResponse convertToDetailResponse(Job job) {
        JobDetailResponse response = new JobDetailResponse();
        response.setId(job.getId());
        response.setEmployerId(job.getEmployer().getId());
        response.setTitle(job.getTitle());
        response.setCategory(job.getCategory());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setAddressDetail(job.getAddressDetail());
        response.setDescription(job.getDescription());
        response.setStatus(job.getStatus().name());
        response.setPostedDate(job.getPostedDate());
        response.setDeadline(job.getDeadline());
        response.setSalary(job.getSalary());
        response.setSalaryType(job.getSalaryType());
        response.setStartTime(job.getStartTime());
        response.setEndTime(job.getEndTime());
        response.setOtherRequirement(job.getOtherRequirement());
        response.setViews(job.getViews());
        response.setApplicants(job.getApplications() != null ? job.getApplications().size() : 0);
        response.setGender(job.getGender());
        response.setAge(job.getAge());
        response.setEducation(job.getEducation());
        
        // JSON을 List로 변환
        try {
            if (job.getQualifications() != null && !job.getQualifications().isEmpty()) {
                response.setQualifications(Arrays.asList(objectMapper.readValue(job.getQualifications(), String[].class)));
            } else {
                response.setQualifications(Collections.emptyList());
            }
            
            if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
                response.setRequirements(Arrays.asList(objectMapper.readValue(job.getRequirements(), String[].class)));
            } else {
                response.setRequirements(Collections.emptyList());
            }
            
            if (job.getWorkDays() != null && !job.getWorkDays().isEmpty()) {
                response.setWorkingDays(Arrays.asList(objectMapper.readValue(job.getWorkDays(), String[].class)));
            } else {
                response.setWorkingDays(Collections.emptyList());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 오류: " + e.getMessage());
        }
        
        return response;
    }
}
