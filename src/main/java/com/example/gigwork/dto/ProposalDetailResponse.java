package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDetailResponse {
    private Long id;
    private JobDetailResponse job;
    private EmployerSummary employer;
    private String status;
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployerSummary {
        private Long id;
        private String companyName;
    }
}

