package com.example.backend.configuration.BatchConfig;

import com.example.backend.customBatch.EventProcessorCustom;
import com.example.backend.customBatch.EventReaderCustom;
import com.example.backend.customBatch.EventWriterCustom;
import com.example.backend.customBatch.JobCompletionNotificationListener;
import com.example.backend.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
//@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job importEventJob(JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importEventJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }


    @Bean
    public Step processEventStep(
            ItemReader<List<Event>> eventReader,
            ItemProcessor<List<Event>, List<Event>> eventProcessor,
            ItemWriter<List<Event>> eventWriter
    ){
        return new StepBuilder("processEventStep",jobRepository)
                .<List<Event>, List<Event>> chunk(10, transactionManager)
                .reader(eventReader)
                .processor(eventProcessor)
                .writer(eventWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<List<Event>> eventReader(EventReaderCustom eventReaderCustom) {
        return eventReaderCustom;
    }

    @Bean
    @StepScope
    public ItemProcessor<List<Event>, List<Event>> eventProcessor(EventProcessorCustom eventProcessorCustom) {
        return eventProcessorCustom;
    }

    @Bean
    @StepScope
    public ItemWriter<List<Event>> eventWriter(EventWriterCustom eventWriterCustom) {
        return eventWriterCustom;
    }
}
