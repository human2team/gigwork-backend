package com.example.gigwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String gender;
    private Integer age;
    private String place;
    private String workDays;
    private String startTime;
    private String endTime;
    private Integer hourlyWage;
    private String requirements;
    private String category;
}
