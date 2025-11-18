package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerSignupRequest {
    private String email;
    private String password;
    private String companyName;
    private String businessNumber;
    private String representativeName;
    private String phone;
    private String address;
}
