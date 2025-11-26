package com.example.gigwork.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gigwork.dto.JobDetailResponse;
import com.example.gigwork.dto.ProposalDetailResponse;
import com.example.gigwork.entity.EmployerProfile;
import com.example.gigwork.entity.Job;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.Proposal;
import com.example.gigwork.repository.EmployerProfileRepository;
import com.example.gigwork.repository.JobRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.ProposalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProposalService {
    @Autowired
    private ProposalRepository proposalRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;
    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Proposal createProposal(Long jobId, Long jobseekerId, Long employerId) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        Optional<JobseekerProfile> jobseekerOpt = jobseekerProfileRepository.findById(jobseekerId);
        Optional<EmployerProfile> employerOpt = employerProfileRepository.findById(employerId);
        if (jobOpt.isEmpty() || jobseekerOpt.isEmpty() || employerOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid job, jobseeker, or employer id");
        }
        // Upsert: 기존 제안이 있으면 상태를 SENT로 되돌려 재제안으로 처리
        Proposal existing = proposalRepository.findFirstByJobIdAndJobseekerIdAndEmployerId(jobId, jobseekerId, employerId);
        if (existing != null) {
            existing.setStatus("SENT");
            return proposalRepository.save(existing);
        }
        Proposal proposal = new Proposal();
        proposal.setJob(jobOpt.get());
        proposal.setJobseeker(jobseekerOpt.get());
        proposal.setEmployer(employerOpt.get());
        return proposalRepository.save(proposal);
    }

    public List<Proposal> getProposalsForJobseeker(Long jobseekerId) {
        return proposalRepository.findByJobseekerId(jobseekerId);
    }

    public List<Proposal> getProposalsForEmployer(Long employerId) {
        return proposalRepository.findByEmployerId(employerId);
    }

    public List<Proposal> getDeclinedForEmployer(Long employerId) {
        return proposalRepository.findByEmployerIdAndStatus(employerId, "REJECTED");
    }

    public ProposalDetailResponse getProposalById(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found"));

        Job job = proposal.getJob();
        JobDetailResponse jobDto = convertToJobDetailResponse(job);

        ProposalDetailResponse.EmployerSummary employerSummary =
                new ProposalDetailResponse.EmployerSummary(
                        proposal.getEmployer().getId(),
                        proposal.getEmployer().getCompanyName()
                );

        ProposalDetailResponse dto = new ProposalDetailResponse();
        dto.setId(proposal.getId());
        dto.setJob(jobDto);
        dto.setEmployer(employerSummary);
        dto.setStatus(proposal.getStatus());
        dto.setCreatedAt(proposal.getCreatedAt());
        return dto;
    }

    private JobDetailResponse convertToJobDetailResponse(Job job) {
        JobDetailResponse response = new JobDetailResponse();
        response.setId(job.getId());
        response.setEmployerId(job.getEmployer().getId());
        response.setTitle(job.getTitle());
        response.setCategory(job.getCategory());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
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

    public void deleteProposal(Long jobId, Long jobseekerId, Long employerId) {
        List<Proposal> proposals = proposalRepository.findByJobId(jobId);
        for (Proposal proposal : proposals) {
            if (proposal.getJobseeker().getId().equals(jobseekerId) && proposal.getEmployer().getId().equals(employerId)) {
                proposalRepository.delete(proposal);
                return;
            }
        }
        throw new IllegalArgumentException("Proposal not found for cancellation");
    }

    public Proposal declineByProposalId(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found"));
        proposal.setStatus("REJECTED");
        return proposalRepository.save(proposal);
    }

    public Proposal declineByTriplet(Long jobId, Long jobseekerId, Long employerId) {
        Proposal proposal = proposalRepository.findFirstByJobIdAndJobseekerIdAndEmployerId(jobId, jobseekerId, employerId);
        if (proposal == null) throw new IllegalArgumentException("Proposal not found");
        proposal.setStatus("REJECTED");
        return proposalRepository.save(proposal);
    }
}
