package com.example.gigwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponse {
    private Long applicationId;
    private Long jobseekerId;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Integer age;
    private String gender;
    private String address;
    private String education;
    private String preferredRegion;
    private String preferredDistrict;
    private String preferredDong;
    private String workDuration;
    private String workDays;
    private String workTime;
    private String mbti;
    private String introduction;
    private String strengths;
    private String muscleStrength;
    private Integer height;
    private Integer weight;
    private String status;
    private Integer suitability;
    private LocalDateTime appliedDate;
    private String appliedDateFormatted;
    private List<LicenseInfo> licenses;
    private List<ExperienceInfo> experiences;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LicenseInfo {
        private Long id;
        private String name;
        private LocalDate issueDate;
        private LocalDate expiryDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceInfo {
        private Long id;
        private String company;
        private String position;
        private String startDate;
        private String endDate;
        private String description;
    }
}
