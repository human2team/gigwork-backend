package com.example.gigwork.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FastApiResponse {
    private Boolean success;
    private String response;
    private Object result;
    private Map<String, Object> state;
}
