package com.enset.batch.facturationspringbatch.config;


import com.enset.batch.facturationspringbatch.DTO.Facture;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<Facture> itemReader;
    @Autowired
    private ItemWriter<Facture> itemWriter;
    @Autowired
    private ItemProcessor<Facture,Facture> itemProcessor;

    @Bean
    public Job processJob(){
        Step step = stepBuilderFactory.get("ETL-data")
                .<Facture,Facture>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
        return jobBuilderFactory.get("facture-data-etl")
                .start(step).build();
    }

    @Bean
    public FlatFileItemReader<Facture> flatFileItemReader(@Value("${inputFile}") Resource resource){
        FlatFileItemReader<Facture> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("Facture_csv");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<Facture> lineMapper() {
        DefaultLineMapper<Facture> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","numero","clientName","montant");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<Facture> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(Facture.class);
        lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        return lineMapper;

    }

}
