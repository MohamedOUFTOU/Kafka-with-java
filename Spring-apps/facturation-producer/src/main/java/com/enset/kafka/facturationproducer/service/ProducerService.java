package com.enset.kafka.facturationproducer.service;

import com.enset.kafka.facturationproducer.DTO.Facture;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class ProducerService implements ApplicationRunner {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        AtomicReference<Long> cnt = new AtomicReference<>(0L);
        System.out.println("---------------------------------------");
        List<String> client_name = Arrays.asList("ATOS","IBM","CAPGEMINI","D-AIM","GOOGLE","FACEBOOK");
        Random random = new Random();
        Gson gson = new Gson();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()->{
            cnt.set(cnt.get() + 1);
            Facture facture = new Facture(cnt.get(),client_name.get(random.nextInt(client_name.size())),random.nextDouble()*20000);
            System.out.println(facture.toString());

            log.info("[SENDING]: Object => "+facture.toString());
            kafkaTemplate.send("FACTURATION",facture.getClientName(),gson.toJson(facture));
        },0,1000, TimeUnit.MILLISECONDS);
    }
}
