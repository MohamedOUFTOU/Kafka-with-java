package com.enset.kafka.facturationproducer;

import com.enset.kafka.facturationproducer.DTO.Facture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@EnableKafka
public class FacturationProducerApplication {


    public static void main(String[] args) {
        SpringApplication.run(FacturationProducerApplication.class, args);
    }


}
