package com.enset.kafka.facturationkafkastream;

import com.enset.kafka.facturationkafkastream.DTO.Facture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableKafkaStreams
public class FacturationKafkaStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacturationKafkaStreamApplication.class, args);
    }

}


@Component
class Processor{

    @Autowired
    public void process(final StreamsBuilder builder){


        KStream<String,Long> counts = builder.stream("FACTURATION", Consumed.with(Serdes.String(),Serdes.String()))
                .map((k,v) -> new KeyValue<>(k,jsonToFacture(v).getClientName()))
                .groupBy((key, value) -> value)
                .count().toStream();

        counts.foreach((k,v) -> {
            System.out.println(k + " -> "+ v);
        });
        counts.to("results-analysis",Produced.with(Serdes.String(),Serdes.Long()));
    }

    private Facture jsonToFacture(String value){
        ObjectMapper objectMapper = new ObjectMapper();
        Facture facture = new Facture();
        try {
            facture = objectMapper.readValue(value,Facture.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return facture;
    }
}
