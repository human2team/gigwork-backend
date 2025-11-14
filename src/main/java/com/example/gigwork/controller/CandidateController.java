package com.example.gigwork.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gigwork.entity.Experience;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.entity.License;
import com.example.gigwork.repository.ExperienceRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;
import com.example.gigwork.repository.LicenseRepository;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private ExperienceRepository experienceRepository;

        @GetMapping("")
        public ResponseEntity<List<Map<String, Object>>> getCandidates(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "license", required = false) String license,
            @RequestParam(name = "minSuitability", required = false, defaultValue = "0") int minSuitability
        ) {
        List<JobseekerProfile> profiles = jobseekerProfileRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (JobseekerProfile profile : profiles) {
            // 필터링
            if (location != null && !location.equals("전체") && (profile.getAddress() == null || !profile.getAddress().contains(location))) continue;
            List<License> licenses = profile.getLicenses();
            if (license != null && !license.equals("전체") && licenses.stream().noneMatch(l -> l.getName().contains(license))) continue;
            // 적합도 점수는 임시로 80~99 랜덤
            int suitability = 80 + new Random(profile.getId()).nextInt(20);
            if (suitability < minSuitability) continue;
            List<Experience> experiences = profile.getExperiences();
            // 검색어 필터
            if (search != null && !search.isEmpty()) {
                String lower = search.toLowerCase();
                boolean matches = (profile.getName() != null && profile.getName().toLowerCase().contains(lower))
                        || licenses.stream().anyMatch(l -> l.getName().toLowerCase().contains(lower))
                        || experiences.stream().anyMatch(e -> e.getCompany() != null && e.getCompany().toLowerCase().contains(lower));
                if (!matches) continue;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", profile.getId());
            map.put("name", profile.getName());
            map.put("age", profile.getBirthDate() != null ? java.time.Period.between(profile.getBirthDate(), java.time.LocalDate.now()).getYears() : null);
            map.put("location", profile.getAddress());
            map.put("licenses", licenses.stream().map(License::getName).collect(Collectors.toList()));
            map.put("experience", experiences.stream().map(e -> (e.getCompany() != null ? e.getCompany() : "") + (e.getPosition() != null ? " - " + e.getPosition() : "") + (e.getStartDate() != null ? " - " + e.getStartDate() : "")).collect(Collectors.toList()));
            map.put("suitability", suitability);
            map.put("available", true); // 실제 구현시 상태 필드 추가 필요
            result.add(map);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCandidateDetail(@org.springframework.web.bind.annotation.PathVariable(name = "id") Long id) {
        JobseekerProfile profile = jobseekerProfileRepository.findById(id).orElse(null);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("지원자를 찾을 수 없습니다.");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", profile.getId());
        result.put("name", profile.getName());
        result.put("email", profile.getUser() != null ? profile.getUser().getEmail() : null);
        result.put("phone", profile.getPhone());
        result.put("region", profile.getPreferredRegion());
        result.put("address", profile.getAddress());
        result.put("education", profile.getEducation());
        result.put("introduction", profile.getIntroduction());
        result.put("age", profile.getBirthDate() != null ? java.time.Period.between(profile.getBirthDate(), java.time.LocalDate.now()).getYears() : null);
        // 자격증
        result.put("licenses", profile.getLicenses().stream().map(l -> {
            Map<String, Object> lic = new HashMap<>();
            lic.put("id", l.getId());
            lic.put("name", l.getName());
            lic.put("issueDate", l.getIssueDate());
            lic.put("expiryDate", l.getExpiryDate());
            return lic;
        }).collect(Collectors.toList()));
        // 경력
        result.put("experiences", profile.getExperiences().stream().map(e -> {
            Map<String, Object> exp = new HashMap<>();
            exp.put("id", e.getId());
            exp.put("company", e.getCompany());
            exp.put("position", e.getPosition());
            exp.put("startDate", e.getStartDate());
            exp.put("endDate", e.getEndDate());
            exp.put("description", e.getDescription());
            return exp;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(result);
    }
}
