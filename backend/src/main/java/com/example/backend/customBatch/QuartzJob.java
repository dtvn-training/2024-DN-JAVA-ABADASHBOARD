package com.example.backend.customBatch;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class QuartzJob implements Job {
    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job importEventJob;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Starting Quartz job to launch Spring batch job...");
        try{
            JobParameters jobParameters= new JobParametersBuilder()
                    .addLong("time",System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(importEventJob,jobParameters);
            log.info("Batch job successfully triggered by Quartz.");
        }catch (Exception e){
            log.error("Error while triggering Spring batch job from Quartz.", e);
            throw new JobExecutionException();
        }
    }
}
