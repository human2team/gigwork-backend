package com.example.gigwork.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.gigwork.entity.Job;
import com.example.gigwork.enums.JobStatus;
import com.example.gigwork.repository.JobRepository;

/**
 * 일자리 공고 관련 스케줄러
 * 매일 자정에 마감일이 지난 공고를 자동으로 CLOSED 상태로 변경
 */
@Component
public class JobScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);
    
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private com.example.gigwork.service.JobService jobService;
    
    /**
     * 매일 자정(00:00:00)에 실행
     * 마감일이 지난 ACTIVE 공고를 CLOSED로 변경
     */
    // 매시간 정각에 실행하여 운영 중 즉시 마감 반영 (운영에서는 하루에 한 번으로 변경 가능)
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void closeExpiredJobs() {
        logger.info("마감일 지난 공고 자동 마감 작업 시작 (스케줄러)");
        int closed = 0;
        try {
            closed = jobService.closeExpiredJobs();
        } catch (Exception e) {
            logger.error("마감 자동 처리 중 오류 발생", e);
        }
        logger.info("마감일 지난 공고 자동 마감 작업 완료: 총 {}건 마감 처리", closed);
    }
}
