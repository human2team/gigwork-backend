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
    
    /**
     * 매일 자정(00:00:00)에 실행
     * 마감일이 지난 ACTIVE 공고를 CLOSED로 변경
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void closeExpiredJobs() {
        logger.info("마감일 지난 공고 자동 마감 작업 시작");
        
        LocalDate today = LocalDate.now();
        
        // ACTIVE 상태인 모든 공고 조회
        List<Job> activeJobs = jobRepository.findByStatus(JobStatus.ACTIVE);
        
        int closedCount = 0;
        
        for (Job job : activeJobs) {
            LocalDate deadline = job.getDeadline();
            
            // 마감일이 설정되어 있고, 오늘 날짜보다 이전인 경우
            if (deadline != null && deadline.isBefore(today)) {
                job.setStatus(JobStatus.CLOSED);
                jobRepository.save(job);
                closedCount++;
                
                logger.info("공고 마감 처리: ID={}, 제목={}, 마감일={}", 
                    job.getId(), job.getTitle(), deadline);
            }
        }
        
        logger.info("마감일 지난 공고 자동 마감 작업 완료: 총 {}건 마감 처리", closedCount);
    }
}
