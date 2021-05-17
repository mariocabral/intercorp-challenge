package com.mariocabral.intercop.challenge.service;

import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.model.KPIClient;
import com.mariocabral.intercop.challenge.repository.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ClientService {

    @Autowired
    private ClientRespository clientRespository;

    public Client newClient(Client client) {
        return clientRespository.save(client);
    }

    public KPIClient kpiClient() {
        int avgOfAge = clientRespository.getAvgOfAge();
        BigDecimal stddevOfAge = clientRespository.getStddevOfAge();
        KPIClient result = new KPIClient();
        result.setAvgAges(avgOfAge);
        result.setStandardDeviationAges(stddevOfAge.setScale(2, RoundingMode.DOWN));
        return result;
    }
}
