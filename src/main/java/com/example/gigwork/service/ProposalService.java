package com.example.gigwork.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gigwork.entity.EmployerProfile;
import com.example.gigwork.entity.Job;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.Proposal;
import com.example.gigwork.repository.EmployerProfileRepository;
import com.example.gigwork.repository.JobRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.ProposalRepository;

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

    public Proposal createProposal(Long jobId, Long jobseekerId, Long employerId) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        Optional<JobseekerProfile> jobseekerOpt = jobseekerProfileRepository.findById(jobseekerId);
        Optional<EmployerProfile> employerOpt = employerProfileRepository.findById(employerId);
        if (jobOpt.isEmpty() || jobseekerOpt.isEmpty() || employerOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid job, jobseeker, or employer id");
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

    public Proposal getProposalById(Long proposalId) {
        return proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found"));
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
}
