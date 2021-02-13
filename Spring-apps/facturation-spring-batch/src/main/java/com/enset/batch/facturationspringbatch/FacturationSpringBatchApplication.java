package com.enset.batch.facturationspringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class FacturationSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacturationSpringBatchApplication.class, args);
    }

    @Bean
    CommandLineRunner start(JobLauncher jobLauncher, Job job){
        return args -> {
            /*
            Map<String, JobParameter> parameterMap = new HashMap<>();
            parameterMap.put("time",new JobParameter(System.currentTimeMillis()));
            JobParameters jobParameters = new JobParameters(parameterMap);
            JobExecution jobExecution = jobLauncher.run(job,jobParameters);
            System.out.println(jobExecution.getStatus());*/
        };
    }
}
