package com.example.gigwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gigwork.entity.SavedJob;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    
    List<SavedJob> findByJobseekerId(Long jobseekerId);
    
    Optional<SavedJob> findByJobseekerIdAndJobId(Long jobseekerId, Long jobId);
    
    void deleteByJobseekerIdAndJobId(Long jobseekerId, Long jobId);
}
