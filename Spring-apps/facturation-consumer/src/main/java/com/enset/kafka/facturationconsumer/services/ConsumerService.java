package com.enset.kafka.facturationconsumer.services;

import com.enset.kafka.facturationconsumer.entities.Facture;
import com.enset.kafka.facturationconsumer.repositories.FactureRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.opencsv.CSVWriter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@Service
public class ConsumerService {

    private FactureRepository factureRepository;

    public ConsumerService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @KafkaListener(topics = "FACTURATION",groupId = "bdcc-group")
    public void onMessage(ConsumerRecord<String,String> consumerRecord){
        System.out.println(consumerRecord.key());
        System.out.println(consumerRecord.value());
        Facture facture = deserilizeValue(consumerRecord.value());
        facture = factureRepository.save(facture);
        addRecordToCsv(facture);
    }

    private Facture deserilizeValue(String value) {
        JsonMapper jsonMapper = new JsonMapper();
        Facture facture = null;
        try {
            facture = jsonMapper.readValue(value, Facture.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return facture;
    }

    private void addRecordToCsv(Facture facture) {
        File file = new File(System.getProperty("user.dir")+"\\data\\bills_recors.csv");
        String line[] = {facture.getId()+"",facture.getNumero()+"",facture.getClientName(),facture.getMontant()+""};
        try {
        if(file.exists()){

                CSVWriter writer = new CSVWriter(new FileWriter(System.getProperty("user.dir")+"\\data\\bills_recors.csv",true));
                writer.writeNext(line);
                writer.flush();

        }else {
            CSVWriter writer = new CSVWriter(new FileWriter(System.getProperty("user.dir")+"\\data\\bills_recors.csv"));
            String headers[] = {"ID","Numero","ClientName","Montant"};
            writer.writeNext(headers);
            writer.writeNext(line);
            writer.flush();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
