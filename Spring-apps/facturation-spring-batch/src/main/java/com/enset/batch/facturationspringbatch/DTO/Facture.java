package com.enset.batch.facturationspringbatch.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Data
@NoArgsConstructor @AllArgsConstructor @ToString
public class Facture {
    private Long id;
    private Long numero;
    private String clientName;
    private double montant;
}
