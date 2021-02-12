package com.enset.kafka.facturationkafkastream.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Facture implements Serializable {
    private Long numero;
    private String clientName;
    private double montant;
}
