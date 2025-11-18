package com.example.gigwork.dto;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
    private Long id;
    private String title;
    private String category;
    private String company;
    private String location;
    private String salary;
    private String salaryType;
    private String description;
    private Integer suitability;
    private String status;      // 추가
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline; // 추가
}
