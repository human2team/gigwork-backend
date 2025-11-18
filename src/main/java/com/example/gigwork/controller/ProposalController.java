package com.example.gigwork.controller;

import com.example.gigwork.entity.Proposal;
import com.example.gigwork.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/proposals")
public class ProposalController {
    @Autowired
    private ProposalService proposalService;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }

    @PostMapping
    public Proposal createProposal(
        @RequestParam("jobId") Long jobId,
        @RequestParam("jobseekerId") Long jobseekerId,
        @RequestParam("employerId") Long employerId) {
        return proposalService.createProposal(jobId, jobseekerId, employerId);
    }

    @DeleteMapping
    public void deleteProposal(
        @RequestParam("jobId") Long jobId,
        @RequestParam("jobseekerId") Long jobseekerId,
        @RequestParam("employerId") Long employerId) {
        proposalService.deleteProposal(jobId, jobseekerId, employerId);
    }

    @GetMapping("/jobseeker/{jobseekerId}")
    public List<Proposal> getProposalsForJobseeker(@PathVariable("jobseekerId") Long jobseekerId) {
        return proposalService.getProposalsForJobseeker(jobseekerId);
    }

    @GetMapping("/employer/{employerId}")
    public List<Proposal> getProposalsForEmployer(@PathVariable Long employerId) {
        return proposalService.getProposalsForEmployer(employerId);
    }

    @GetMapping("/{proposalId}")
    public Proposal getProposalById(@PathVariable Long proposalId) {
        return proposalService.getProposalById(proposalId);
    }
}
