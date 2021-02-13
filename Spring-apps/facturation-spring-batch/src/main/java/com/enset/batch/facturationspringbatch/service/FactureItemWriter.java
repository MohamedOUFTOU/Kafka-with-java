package com.enset.batch.facturationspringbatch.service;

import com.enset.batch.facturationspringbatch.DTO.Facture;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FactureItemWriter implements ItemWriter<Facture> {
    @Override
    public void write(List<? extends Facture> list) throws Exception {
        list.forEach( item -> {
            System.out.println(item);
        });
    }
}
