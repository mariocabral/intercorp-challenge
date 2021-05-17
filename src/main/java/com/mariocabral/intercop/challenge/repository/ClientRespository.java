package com.mariocabral.intercop.challenge.repository;

import com.mariocabral.intercop.challenge.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRespository extends CrudRepository<Client, Long>{
}
