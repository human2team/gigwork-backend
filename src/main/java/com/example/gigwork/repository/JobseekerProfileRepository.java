package com.example.gigwork.repository;

import com.example.gigwork.entity.JobseekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobseekerProfileRepository extends JpaRepository<JobseekerProfile, Long> {
    
    // User ID로 프로필 찾기
    Optional<JobseekerProfile> findByUserId(Long userId);
}
