package com.example.backend.service.GoogleAnalyticService.Impl;

import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticJobScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAnalyticJobSchedulerImpl implements GoogleAnalyticJobScheduler {
    private final JobLauncher jobLauncher;
    private final Job analyticJob;

    @Override
    @Scheduled(cron = "0 0 12 * * ?")
    public void runAnalyticsJob() {
        try{
            JobParameters parameter= new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(analyticJob, parameter);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
