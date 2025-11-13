package com.example.gigwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gigwork.entity.EmployerProfile;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    
    /**
     * User ID로 사업자 프로필 조회
     */
    Optional<EmployerProfile> findByUserId(Long userId);
    
    /**
     * 사업자등록번호로 조회
     */
    Optional<EmployerProfile> findByBusinessNumber(String businessNumber);
    
    /**
     * 사업자등록번호 존재 여부 확인
     */
    boolean existsByBusinessNumber(String businessNumber);
}
