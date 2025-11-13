package com.example.gigwork.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gigwork.dto.JobResponse;
import com.example.gigwork.entity.Job;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.SavedJob;
import com.example.gigwork.exception.ApiException;
import com.example.gigwork.repository.JobRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.SavedJobRepository;

@Service
public class SavedJobService {

    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    /**
     * 일자리 저장
     */
    @Transactional
    public void saveJob(Long userId, Long jobId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("일자리를 찾을 수 없습니다."));

        // 이미 저장된 경우 무시
        if (savedJobRepository.findByJobseekerIdAndJobId(profile.getId(), jobId).isPresent()) {
            return;
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setJobseeker(profile);
        savedJob.setJob(job);
        savedJobRepository.save(savedJob);
    }

    /**
     * 일자리 저장 해제
     */
    @Transactional
    public void unsaveJob(Long userId, Long jobId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        savedJobRepository.deleteByJobseekerIdAndJobId(profile.getId(), jobId);
    }

    /**
     * 저장된 일자리 목록 조회
     */
    @Transactional(readOnly = true)
    public List<JobResponse> getSavedJobs(Long userId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        List<SavedJob> savedJobs = savedJobRepository.findByJobseekerId(profile.getId());
        
        return savedJobs.stream()
                .map(savedJob -> convertToResponse(savedJob.getJob()))
                .collect(Collectors.toList());
    }

    /**
     * 일자리가 저장되었는지 확인
     */
    @Transactional(readOnly = true)
    public boolean isJobSaved(Long userId, Long jobId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        return savedJobRepository.findByJobseekerIdAndJobId(profile.getId(), jobId).isPresent();
    }

    /**
     * Job Entity -> JobResponse DTO 변환
     */
    private JobResponse convertToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setSalary(job.getSalary());
        response.setDescription(job.getDescription());
        response.setJobType(job.getJobType());
        response.setStatus(job.getStatus().name());
        response.setPostedDate(job.getPostedDate());
        response.setDeadline(job.getDeadline());
        response.setWorkHours(job.getWorkHours());
        response.setWorkDays(job.getWorkDays());
        response.setViews(job.getViews());
        if (job.getEmployer() != null) {
            response.setEmployerId(job.getEmployer().getId());
        }
        response.setSaved(true); // 저장된 목록이므로 true
        return response;
    }
}
