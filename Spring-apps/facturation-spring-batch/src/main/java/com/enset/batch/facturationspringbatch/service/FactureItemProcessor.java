package com.enset.batch.facturationspringbatch.service;

import com.enset.batch.facturationspringbatch.DTO.Facture;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class FactureItemProcessor implements ItemProcessor<Facture,Facture> {


    @Override
    public Facture process(Facture facture) throws Exception {
        return facture;
    }
}
