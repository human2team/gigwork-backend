package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseRequest {
    private String name;
    private LocalDate issueDate;
    private LocalDate expiryDate;
}
