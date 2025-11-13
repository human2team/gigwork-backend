package com.example.gigwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.gigwork.entity.Application;
import com.example.gigwork.enums.ApplicationStatus;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    // 구직자별 지원 내역 조회
    List<Application> findByJobseekerId(Long jobseekerId);
    
    // 일자리별 지원자 조회 (JobseekerProfile과 User만 fetch)
    @Query("SELECT DISTINCT a FROM Application a " +
           "LEFT JOIN FETCH a.jobseeker js " +
           "LEFT JOIN FETCH js.user " +
           "WHERE a.job.id = :jobId")
    List<Application> findByJobIdWithDetails(@Param("jobId") Long jobId);
    
    // 일자리별 지원자 조회 (기본)
    List<Application> findByJobId(Long jobId);
    
    // 구직자 + 일자리로 지원 여부 확인
    Optional<Application> findByJobseekerIdAndJobId(Long jobseekerId, Long jobId);
    
    // 상태별 지원 내역 조회
    List<Application> findByJobseekerIdAndStatus(Long jobseekerId, ApplicationStatus status);
}
