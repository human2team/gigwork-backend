package com.example.gigwork.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer", "handler", "applications", "employer", "proposals", "jobseekerProfiles"
    })
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobseeker_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer", "handler", "user", "licenses", "experiences", "applications", "proposals"
    })
    private JobseekerProfile jobseeker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer", "handler", "user", "jobs", "proposals"
    })
    private EmployerProfile employer;

    private String status; // SENT, ACCEPTED, REJECTED, etc.

    private LocalDateTime createdAt;

    public Proposal() {
        this.createdAt = LocalDateTime.now();
        this.status = "SENT";
    }

    // Getters and setters
    public Long getId() { return id; }
    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
    public JobseekerProfile getJobseeker() { return jobseeker; }
    public void setJobseeker(JobseekerProfile jobseeker) { this.jobseeker = jobseeker; }
    public EmployerProfile getEmployer() { return employer; }
    public void setEmployer(EmployerProfile employer) { this.employer = employer; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
