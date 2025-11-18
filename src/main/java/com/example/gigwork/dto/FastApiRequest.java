package com.example.gigwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastApiRequest {
    private String userId;
    private String text;
    private Map<String, Object> condition;
    private Boolean search;
}
