package com.enset.kafka.facturationconsumer.repositories;

import com.enset.kafka.facturationconsumer.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FactureRepository extends JpaRepository<Facture,Long> {
}
