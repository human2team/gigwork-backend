package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobseekerSignupRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String birthDate;
}
