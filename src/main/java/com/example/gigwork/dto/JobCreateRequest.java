
package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateRequest {
    private String title;
    private String category;
    private String company;
    private String location;
    private String description;
    private List<String> qualifications;
    private List<String> requirements;
    private String otherRequirement;
    private List<String> workingDays;
    private String startTime;
    private String endTime;
    private String salary;
    private String salaryType;
    private LocalDate deadline;
    private String gender;
    private String age;
    private String education;
}
