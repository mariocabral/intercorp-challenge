package com.mariocabral.intercop.challenge.service;

import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.repository.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRespository clientRespository;

    public Client newClient(Client client) {
        return clientRespository.save(client);
    }
}
