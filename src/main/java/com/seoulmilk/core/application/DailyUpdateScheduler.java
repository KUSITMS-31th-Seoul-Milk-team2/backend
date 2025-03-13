package com.seoulmilk.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class DailyUpdateScheduler {
    private final JobLauncher jobLauncher;
    private final Job bulkUpdateJob;

    @Scheduled(cron = "0 0 0 * * *")
    public void runBatchJob() throws Exception {
        try {
            jobLauncher.run(bulkUpdateJob, new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters());
        } catch (Exception e) {
            log.error("[runBatchJob()] 배치 작업 중 에러가 발생했습니다." + e.getLocalizedMessage());
            throw e;
        }
    }
}
