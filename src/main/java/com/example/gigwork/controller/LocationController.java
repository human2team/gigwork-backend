package com.example.gigwork.controller;

import com.example.gigwork.cache.RegionCache;
import com.example.gigwork.model.Region;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LocationController {
    private final RegionCache regionCache;

    public LocationController(RegionCache regionCache) {
        this.regionCache = regionCache;
    }

    @GetMapping("/regions")
    public ResponseEntity<?> getRegions() {
        return ResponseEntity.ok(
            regionCache.getRegions().stream()
                .filter(r -> r.getSgg().equals("000") && r.getUmd().equals("000"))
                .toList()
        );
    }

    @GetMapping("/districts")
    public ResponseEntity<?> getDistricts(@RequestParam String region) {
        System.out.println("[districts] 받은 region 파라미터: " + region);
        String sidoCode = region.length() >= 2 ? region.substring(0, 2) : region;
        System.out.println("[districts] 추출한 sido 코드: " + sidoCode);
        
        // 선택된 시/도 이름 찾기
        String sidoName = regionCache.getRegions().stream()
            .filter(r -> r.getCode().equals(region))
            .map(Region::getName)
            .findFirst()
            .orElse("");
        
        boolean isSpecialCity = sidoName.contains("특별시") || sidoName.contains("광역시");
        
        List<Region> allDistricts = regionCache.getRegions().stream()
            .filter(r -> r.getSido().equals(sidoCode)
                && !r.getSgg().equals("000")
                && r.getUmd().equals("000"))
            .collect(Collectors.toList());
        
        // 하위 구/군이 있는 시는 제외 (예: 포항시 제외, 포항시 남구/북구만 표시)
        List<Map<String, String>> filteredDistricts = allDistricts.stream()
            .filter(district -> {
                String sggCode = district.getSgg();
                // 같은 시/도에서 이 district보다 더 세분화된 구/군이 있는지 확인
                boolean hasSubDistricts = allDistricts.stream()
                    .anyMatch(other -> !other.getSgg().equals(sggCode) 
                        && other.getName().startsWith(district.getName()));
                return !hasSubDistricts;
            })
            .map(r -> {
                String cleanedName = cleanDistrictName(r.getName(), isSpecialCity);
                return Map.of(
                    "code", r.getCode(),
                    "name", cleanedName,
                    "sido", r.getSido(),
                    "sgg", r.getSgg(),
                    "umd", r.getUmd()
                );
            })
            .collect(Collectors.toList());
        
        System.out.println("[districts] 결과 개수: " + filteredDistricts.size());
        return ResponseEntity.ok(filteredDistricts);
    }

    @GetMapping("/dongs")
    public ResponseEntity<?> getDongs(@RequestParam String district) {
        System.out.println("[dongs] 받은 district 파라미터: " + district);
        
        String sidoCode = district.length() >= 2 ? district.substring(0, 2) : "";
        String sggCode = district.length() >= 5 ? district.substring(2, 5) : district;
        
        System.out.println("[dongs] 추출한 sido 코드: " + sidoCode);
        System.out.println("[dongs] 추출한 sgg 코드: " + sggCode);
        
        var result = regionCache.getRegions().stream()
            .filter(r -> r.getSido().equals(sidoCode)
                && r.getSgg().equals(sggCode)
                && !r.getUmd().equals("000"))
            .map(r -> {
                String cleanedName = cleanDongName(r.getName());
                // "리" 또는 "가"로 끝나는 것 제외
                if (cleanedName == null || cleanedName.endsWith("리") || cleanedName.endsWith("가")) {
                    return null;
                }
                return Map.of(
                    "code", r.getCode(),
                    "name", cleanedName,
                    "sido", r.getSido(),
                    "sgg", r.getSgg(),
                    "umd", r.getUmd()
                );
            })
            .filter(item -> item != null)
            .collect(Collectors.toList());
        
        System.out.println("[dongs] 결과 개수: " + result.size());
        return ResponseEntity.ok(result);
    }
    
    /**
     * 구/군 이름 정리
     * - 특별시/광역시: "서울특별시 강남구" -> "강남구" (시 제거)
     * - 도: "경기도 수원시 장안구" -> "수원시 장안구" (도만 제거)
     */
    private String cleanDistrictName(String fullName, boolean isSpecialCity) {
        if (fullName == null) return "";
        
        if (isSpecialCity) {
            // 특별시/광역시: "서울특별시 강남구" -> "강남구"
            if (fullName.contains("시 ")) {
                String[] parts = fullName.split("시 ", 2);
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    return parts[1].trim();
                }
            }
        } else {
            // 도: "경기도 수원시 장안구" -> "수원시 장안구"
            if (fullName.contains("도 ")) {
                String[] parts = fullName.split("도 ", 2);
                if (parts.length > 1) {
                    return parts[1].trim();
                }
            }
        }
        
        return fullName;
    }
    
    /**
     * 동/읍/면 이름 정리
     * "경상북도 포항시 남구 효자동" -> "효자동"
     */
    private String cleanDongName(String fullName) {
        if (fullName == null) return "";
        
        // 마지막 공백 이후의 텍스트만 추출
        int lastSpaceIndex = fullName.lastIndexOf(' ');
        if (lastSpaceIndex > 0) {
            return fullName.substring(lastSpaceIndex + 1).trim();
        }
        
        return fullName;
    }
}