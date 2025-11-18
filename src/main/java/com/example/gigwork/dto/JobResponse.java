package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String category;
    private String company;
    private String location;
    private String salary;
    private String description;
    private String jobType;
    private String status;
    private LocalDate postedDate;
    private LocalDate deadline;
    private String workHours;
    private String workDays;
    private Integer views;
    private Long employerId;
    private boolean saved;
    private boolean applied;
    private String gender;
    private String age;
    private String education;
}