package com.example.staffmanagerapi.batch;


import com.example.staffmanagerapi.model.Collaborator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {


    private final Reader reader;
    private final Writer writer;
    private final Processor processor;

    @Autowired
    public BatchConfiguration(Reader reader, Writer writer, Processor processor1) {
        this.reader = reader;
        this.writer = writer;
        this.processor = processor1;
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository,
                              Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .flow(step1)
                .end()
                .build();
    }


    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .<Collaborator, Collaborator>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
