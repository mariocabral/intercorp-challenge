package com.mariocabral.intercop.challenge.repository;

import com.mariocabral.intercop.challenge.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ClientRespository extends PagingAndSortingRepository<Client, Long> {

    @Query(value = "SELECT AVG(c.age) FROM Client as c", nativeQuery = true)
    public Integer getAvgOfAge();

    @Query(value = "SELECT STDDEV_SAMP(c.age) FROM Client as c", nativeQuery = true)
    public BigDecimal getStddevOfAge();
}
