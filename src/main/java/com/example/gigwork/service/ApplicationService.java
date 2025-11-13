package com.example.gigwork.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gigwork.dto.ApplicantResponse;
import com.example.gigwork.dto.ApplicationRequest;
import com.example.gigwork.dto.ApplicationResponse;
import com.example.gigwork.entity.Application;
import com.example.gigwork.entity.Job;
import com.example.gigwork.entity.JobseekerProfile;
import com.example.gigwork.enums.ApplicationStatus;
import com.example.gigwork.exception.ApiException;
import com.example.gigwork.repository.ApplicationRepository;
import com.example.gigwork.repository.JobRepository;
import com.example.gigwork.repository.JobseekerProfileRepository;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobseekerProfileRepository jobseekerProfileRepository;

    @Autowired
    private JobRepository jobRepository;

    /**
     * 일자리 지원
     */
    @Transactional
    public ApplicationResponse applyJob(Long userId, ApplicationRequest request) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ApiException("일자리를 찾을 수 없습니다."));

        // 이미 지원한 경우 체크
        applicationRepository.findByJobseekerIdAndJobId(profile.getId(), job.getId())
                .ifPresent(app -> {
                    throw new ApiException("이미 지원한 일자리입니다.");
                });

        // 적합도 자동 계산
        int suitability = calculateSuitability(profile, job);

        // 새로운 지원서 생성
        Application application = new Application();
        application.setJobseeker(profile);
        application.setJob(job);
        application.setStatus(ApplicationStatus.PENDING);
        application.setSuitability(suitability);

        Application saved = applicationRepository.save(application);
        return convertToResponse(saved);
    }

    /**
     * 지원한 일자리 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplications(Long userId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        List<Application> applications = applicationRepository.findByJobseekerId(profile.getId());
        return applications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 지원 취소 (삭제)
     */
    @Transactional
    public void cancelApplication(Long userId, Long applicationId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("지원 내역을 찾을 수 없습니다."));

        // 본인의 지원서인지 확인
        if (!application.getJobseeker().getId().equals(profile.getId())) {
            throw new ApiException("본인의 지원 내역만 취소할 수 있습니다.");
        }

        applicationRepository.delete(application);
    }

    /**
     * 지원 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isJobApplied(Long userId, Long jobId) {
        JobseekerProfile profile = jobseekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("프로필을 찾을 수 없습니다."));

        return applicationRepository.findByJobseekerIdAndJobId(profile.getId(), jobId)
                .isPresent();
    }

    /**
     * 사업자용 - 특정 공고에 지원한 지원자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ApplicantResponse> getApplicantsByJob(Long jobId) {
        // 공고 존재 확인
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("일자리를 찾을 수 없습니다."));

        // 해당 공고에 지원한 모든 지원서 조회 (자격증, 경력 포함)
        List<Application> applications = applicationRepository.findByJobIdWithDetails(jobId);

        // Application -> ApplicantResponse 변환
        return applications.stream()
                .map(this::convertToApplicantResponse)
                .collect(Collectors.toList());
    }

    /**
     * 사업자용 - 지원 상태 업데이트
     */
    @Transactional
    public void updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("지원 내역을 찾을 수 없습니다."));

        // 상태 문자열을 enum으로 변환
        ApplicationStatus applicationStatus;
        try {
            switch (status) {
                case "대기":
                    applicationStatus = ApplicationStatus.PENDING;
                    break;
                case "합격":
                    applicationStatus = ApplicationStatus.ACCEPTED;
                    break;
                case "불합격":
                    applicationStatus = ApplicationStatus.REJECTED;
                    break;
                default:
                    throw new ApiException("유효하지 않은 상태값입니다: " + status);
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException("유효하지 않은 상태값입니다: " + status);
        }

        application.setStatus(applicationStatus);
        applicationRepository.save(application);
    }

    /**
     * Application Entity -> ApplicationResponse DTO 변환 (구직자용)
     */
    private ApplicationResponse convertToResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setStatus(application.getStatus().name());
        response.setSuitability(application.getSuitability());
        response.setAppliedDate(application.getAppliedDate());

        // Job 정보 포함
        Job job = application.getJob();
        response.setJobId(job.getId());
        response.setJobTitle(job.getTitle());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setSalary(job.getSalary());
        response.setDescription(job.getDescription());
        response.setJobType(job.getJobType());

        // 날짜 포맷팅 (프론트엔드용)
        response.setPosted(formatDate(application.getAppliedDate()));

        return response;
    }

    /**
     * Application Entity -> ApplicantResponse DTO 변환 (사업자용)
     */
    private ApplicantResponse convertToApplicantResponse(Application application) {
        ApplicantResponse response = new ApplicantResponse();
        JobseekerProfile jobseeker = application.getJobseeker();

        // Application 정보
        response.setApplicationId(application.getId());
        response.setStatus(application.getStatus().name());
        response.setSuitability(application.getSuitability());
        response.setAppliedDate(application.getAppliedDate());
        response.setAppliedDateFormatted(formatDate(application.getAppliedDate()));

        // JobseekerProfile 정보
        response.setJobseekerId(jobseeker.getId());
        response.setName(jobseeker.getName());
        response.setEmail(jobseeker.getUser().getEmail()); // User의 email 가져오기
        response.setPhone(jobseeker.getPhone());
        response.setBirthDate(jobseeker.getBirthDate());
        
        // 나이 계산
        if (jobseeker.getBirthDate() != null) {
            int age = Period.between(jobseeker.getBirthDate(), LocalDate.now()).getYears();
            response.setAge(age);
        }

        response.setGender(jobseeker.getGender());
        response.setAddress(jobseeker.getAddress());
        response.setEducation(jobseeker.getEducation());
        response.setPreferredRegion(jobseeker.getPreferredRegion());
        response.setPreferredDistrict(jobseeker.getPreferredDistrict());
        response.setPreferredDong(jobseeker.getPreferredDong());
        response.setWorkDuration(jobseeker.getWorkDuration());
        response.setWorkDays(jobseeker.getWorkDays());
        response.setWorkTime(jobseeker.getWorkTime());
        response.setMbti(jobseeker.getMbti());
        response.setIntroduction(jobseeker.getIntroduction());
        response.setStrengths(jobseeker.getStrengths());
        
        // 신체 정보
        if (jobseeker.getMuscleStrength() != null) {
            response.setMuscleStrength(jobseeker.getMuscleStrength().name());
        }
        response.setHeight(jobseeker.getHeight());
        response.setWeight(jobseeker.getWeight());
        
        // 자격증 정보 (LAZY 로딩 강제 초기화)
        List<ApplicantResponse.LicenseInfo> licenses = new ArrayList<>();
        try {
            // size()를 호출해서 컬렉션 초기화
            jobseeker.getLicenses().size();
            licenses = jobseeker.getLicenses().stream()
                .map(license -> new ApplicantResponse.LicenseInfo(
                    license.getId(),
                    license.getName(),
                    license.getIssueDate(),
                    license.getExpiryDate()
                ))
                .collect(Collectors.toList());
        } catch (Exception e) {
            // LAZY 로딩 실패 시 빈 리스트
            licenses = new ArrayList<>();
        }
        response.setLicenses(licenses);
        
        // 경력 정보 (LAZY 로딩 강제 초기화)
        List<ApplicantResponse.ExperienceInfo> experiences = new ArrayList<>();
        try {
            // size()를 호출해서 컬렉션 초기화
            jobseeker.getExperiences().size();
            experiences = jobseeker.getExperiences().stream()
                .map(exp -> new ApplicantResponse.ExperienceInfo(
                    exp.getId(),
                    exp.getCompany(),
                    exp.getPosition(),
                    exp.getStartDate(),
                    exp.getEndDate(),
                    exp.getDescription()
                ))
                .collect(Collectors.toList());
        } catch (Exception e) {
            // LAZY 로딩 실패 시 빈 리스트
            experiences = new ArrayList<>();
        }
        response.setExperiences(experiences);

        return response;
    }

    /**
     * 적합도 계산 (구직자 프로필과 공고 요구사항 비교)
     */
    private int calculateSuitability(JobseekerProfile profile, Job job) {
        int score = 0;
        
        // 1. 성별 매칭 (20점)
        if (job.getGender() == null || job.getGender().equals("무관") || job.getGender().isEmpty()) {
            score += 20; // 무관이면 만점
        } else if (profile.getGender() != null && profile.getGender().equals(job.getGender())) {
            score += 20; // 일치하면 만점
        }
        // 불일치하면 0점
        
        // 2. 연령 매칭 (20점)
        if (job.getAge() == null || job.getAge().equals("무관") || job.getAge().isEmpty()) {
            score += 20; // 무관이면 만점
        } else if (profile.getBirthDate() != null) {
            int age = Period.between(profile.getBirthDate(), LocalDate.now()).getYears();
            String ageGroup = getAgeGroup(age);
            if (ageGroup.equals(job.getAge())) {
                score += 20; // 일치하면 만점
            } else if (isAdjacentAgeGroup(ageGroup, job.getAge())) {
                score += 10; // 인접 연령대면 반점
            }
        }
        
        // 3. 학력 매칭 (20점)
        if (job.getEducation() == null || job.getEducation().equals("무관") || job.getEducation().isEmpty()) {
            score += 20; // 무관이면 만점
        } else if (profile.getEducation() != null) {
            int profileLevel = getEducationLevel(profile.getEducation());
            int jobLevel = getEducationLevel(job.getEducation());
            if (profileLevel >= jobLevel) {
                score += 20; // 요구 학력 이상이면 만점
            } else if (profileLevel == jobLevel - 1) {
                score += 10; // 1단계 낮으면 반점
            }
        }
        
        // 4. 자격증 보유 (20점)
        if (profile.getLicenses() != null && !profile.getLicenses().isEmpty()) {
            score += 20; // 자격증이 있으면 가점
        } else {
            score += 5; // 없어도 기본 점수
        }
        
        // 5. 경력 보유 (20점)
        if (profile.getExperiences() != null && !profile.getExperiences().isEmpty()) {
            score += 20; // 경력이 있으면 가점
        } else {
            score += 5; // 없어도 기본 점수
        }
        
        // 최종 점수 (0-100 범위로 정규화)
        return Math.min(100, Math.max(0, score));
    }
    
    /**
     * 나이를 연령대 그룹으로 변환
     */
    private String getAgeGroup(int age) {
        if (age < 30) return "20대";
        if (age < 40) return "30대";
        if (age < 50) return "40대";
        if (age < 60) return "50대";
        return "60대 이상";
    }
    
    /**
     * 인접 연령대 확인
     */
    private boolean isAdjacentAgeGroup(String group1, String group2) {
        String[] ageGroups = {"20대", "30대", "40대", "50대", "60대 이상"};
        int index1 = -1, index2 = -1;
        for (int i = 0; i < ageGroups.length; i++) {
            if (ageGroups[i].equals(group1)) index1 = i;
            if (ageGroups[i].equals(group2)) index2 = i;
        }
        return Math.abs(index1 - index2) == 1;
    }
    
    /**
     * 학력을 레벨로 변환 (높을수록 높은 학력)
     */
    private int getEducationLevel(String education) {
        if (education == null || education.equals("무관")) return 0;
        switch (education) {
            case "고졸": return 1;
            case "대졸": return 2;
            case "석사": return 3;
            case "박사": return 4;
            default: return 0;
        }
    }
    
    /**
     * 날짜 포맷팅 헬퍼
     */
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        
        LocalDate applicationDate = dateTime.toLocalDate();
        LocalDate today = LocalDate.now();
        long days = java.time.temporal.ChronoUnit.DAYS.between(applicationDate, today);
        
        if (days == 0) return "오늘";
        if (days == 1) return "어제";
        if (days < 7) return days + "일 전";
        if (days < 30) return (days / 7) + "주 전";
        if (days < 365) return (days / 30) + "개월 전";
        
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
