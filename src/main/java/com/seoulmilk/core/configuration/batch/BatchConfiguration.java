package com.seoulmilk.core.configuration.batch;

import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.time.LocalDateTime;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

    private final ValidReceiptRepository validReceiptRepository;

    @Bean
    public Tasklet bulkUpdateTasklet() {
        return (contribution, chunkContext) -> {
            LocalDateTime cutoff = LocalDateTime.now().minusYears(1);
            int updatedCount = validReceiptRepository.bulkUpdateExpiredReceipts(cutoff);
            contribution.incrementWriteCount(updatedCount);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step bulkUpdateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            Tasklet bulkUpdateTasklet
    ) {
        return new StepBuilder("bulkUpdateStep", jobRepository)
                .tasklet(bulkUpdateTasklet, transactionManager)
                .transactionAttribute(
                        new DefaultTransactionAttribute() {{
                            setTimeout(60);
                            setIsolationLevel(Isolation.READ_COMMITTED.value());
                        }}
                )
                .build();
    }

    @Bean
    public Job bulkUpdateJob(JobRepository jobRepository, Step bulkUpdateStep) {
        return new JobBuilder("bulkUpdateJob", jobRepository)
                .start(bulkUpdateStep)
                .build();
    }
}
