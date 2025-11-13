package com.example.gigwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gigwork.entity.Job;
import com.example.gigwork.enums.JobStatus;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    // 상태별 일자리 조회
    List<Job> findByStatus(JobStatus status);
    
    // 고용주별 일자리 조회 (EmployerProfile의 user_id로 조회)
    List<Job> findByEmployerUserId(Long userId);
    
    // 고용주별 일자리 조회 (EmployerProfile의 id로 조회)
    List<Job> findByEmployerId(Long employerId);
    
    // 상태별 + 고용주별 일자리 조회
    List<Job> findByEmployerIdAndStatus(Long employerId, JobStatus status);
}
