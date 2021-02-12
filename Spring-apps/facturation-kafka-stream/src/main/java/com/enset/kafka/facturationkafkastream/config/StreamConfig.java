package com.enset.kafka.facturationkafkastream.config;

import com.enset.kafka.facturationkafkastream.DTO.Facture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StreamConfig {
    Map<String,Object> conf;
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration(){
        conf = new HashMap<>();
        conf.put(StreamsConfig.APPLICATION_ID_CONFIG,"Facturation-event");
        conf.put(StreamsConfig.CLIENT_ID_CONFIG,"streams-app");
        conf.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        conf.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        conf.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());
        conf.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,1000);
        return new KafkaStreamsConfiguration(conf);
    }

    public StreamsBuilderFactoryBean factoryBean(){
        return new StreamsBuilderFactoryBean(kafkaStreamsConfiguration());
    }



    public KStream<String,Long> analyse() throws Exception {
        KStream<String,String> stream = factoryBean().getObject().stream("FACTURATION", Consumed.with(Serdes.String(),Serdes.String()));
        KStream<String,Long> counts = stream
                .map((k,v) -> new KeyValue<>(k,jsonToFacture(v)))
                .groupBy((k,v) -> v.getClientName())
                .count(Materialized.as("client-analysis"))
                .toStream();

        counts.to("facturation-analysis", Produced.with(Serdes.String(),Serdes.Long()));

        return counts;
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
