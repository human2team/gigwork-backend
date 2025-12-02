
package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailResponse {
    private Long id;
    private Long employerId;
    private String title;
    private String category;
    private String company;
    private String location;
    private String addressDetail;
    private String description;
    private List<String> qualifications;
    private List<String> requirements;
    private String otherRequirement;
    private List<String> workingDays;
    private String startTime;
    private String endTime;
    private String salary;
    private String salaryType;
    private String status;
    private LocalDate postedDate;
    private LocalDate deadline;
    private Integer views;
    private Integer applicants;
    private String gender;
    private String age;
    private String education;
}
