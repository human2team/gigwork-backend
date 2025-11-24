package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.gigwork.enums.MuscleStrength;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobseekerProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String education;
    private String preferredRegion;
    private String preferredDistrict;
    private String preferredDong;
    private String workDuration;
    private String workDays;
    private String workTime;
    private MuscleStrength muscleStrength;
    private Integer height;
    private Integer weight;
    private String strengths;
    private String mbti;
    private String introduction;
    // 희망 업직종 (소분류) - 코드/명 콤마 구분
    private String desiredCategoryCodes;
    private String desiredCategoryNames;
}
