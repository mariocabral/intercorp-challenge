package com.mariocabral.intercop.challenge.service;

import com.mariocabral.intercop.challenge.calculator.GenderCalculator;
import com.mariocabral.intercop.challenge.calculator.GenderCalculatorGenderizeClient;
import com.mariocabral.intercop.challenge.calculator.LifeExpectancyCalculator;
import com.mariocabral.intercop.challenge.exception.InvalidClientException;
import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.model.KPIClient;
import com.mariocabral.intercop.challenge.repository.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ClientService {

    @Autowired
    private ClientRespository clientRespository;

    public Client newClient(Client client)  {
        validate(client);
        GenderCalculator genderCalculator = new GenderCalculatorGenderizeClient();
        GenderCalculator.Gender gender = genderCalculator.calculateGender(client.getName());
        LifeExpectancyCalculator lifeExpectancyCalculator = new LifeExpectancyCalculator() {};
        Date expectedLife = lifeExpectancyCalculator.calculateLifeExpectancy(gender, client.getAge(), client.getBirthDate());
        client.setExpectedDateOfDeath(expectedLife);
        return clientRespository.save(client);
    }

    private void validate(Client client) {
        validateField(client.getName(), "name");
        validateField(client.getLastName(), "latName");
        validateField(client.getAge(), "age");
        validateField(client.getBirthDate(), "birthDate");
    }

    private void validateField(Object fieldValue, String fieldName){
        if (ObjectUtils.isEmpty(fieldValue)){
            throw new InvalidClientException("Invalid Client, field " + fieldName +" required");
        }
    }


    public KPIClient kpiClient() {
        int avgOfAge = clientRespository.getAvgOfAge();
        BigDecimal stddevOfAge = clientRespository.getStddevOfAge();
        KPIClient result = new KPIClient();
        result.setAvgAges(avgOfAge);
        result.setStandardDeviationAges(stddevOfAge.setScale(2, RoundingMode.DOWN));
        return result;
    }

    public Page<Client> listClient(PageRequest pageRequest) {
        return clientRespository.findAll(pageRequest);
    }
}
