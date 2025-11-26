package com.example.gigwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gigwork.entity.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByJobseekerId(Long jobseekerId);
    List<Proposal> findByEmployerId(Long employerId);
    List<Proposal> findByJobId(Long jobId);

    List<Proposal> findByEmployerIdAndStatus(Long employerId, String status);

    Proposal findFirstByJobIdAndJobseekerIdAndEmployerId(Long jobId, Long jobseekerId, Long employerId);
}
