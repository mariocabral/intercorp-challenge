package com.mariocabral.intercop.challenge.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.model.KPIClient;
import com.mariocabral.intercop.challenge.repository.ClientRespository;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@WebAppConfiguration
class ClientControllerTest {

    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private ClientRespository clientRespository;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }


    @Test
    void newClient() throws Exception {
        String uri = "/api/creacliente";
        Client client = new Client();
        client.setAge(29);
        client.setName("TestName");
        client.setLastName("TestLastName");
        Date birthDate = new Date();
        client.setBirthDate(birthDate);

        String inputJson = mapToJson(client);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Client result = mapFromJson(content, Client.class);
        assertEquals("TestName", result.getName());
        assertEquals("TestLastName", result.getLastName());
        assertEquals(29, result.getAge());
        assertEquals(birthDate, result.getBirthDate());
        assertNotNull(result.getId());
    }




    @Test
    void kpiClient() throws Exception {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        populateDB(stats);
        String uri = "/api/kpideclientes";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        KPIClient result = mapFromJson(content, KPIClient.class);
        Double meanAge = stats.getMean();
        assertEquals(meanAge.intValue(), result.getAvgAges());
        BigDecimal standardDeviation = new BigDecimal(stats.getStandardDeviation());
        standardDeviation = standardDeviation.setScale(2, RoundingMode.DOWN);
        assertEquals(standardDeviation, result.getStandardDeviationAges());
    }

    private void populateDB(DescriptiveStatistics stats) {
        int ageMin = 1;
        int ageMax = 100;

        for (int i = 0; i < 1000; i++){
            Client c = new Client();
            c.setName("Test_" + i);
            c.setLastName("LastNAme_"+ i);
            c.setBirthDate(new Date());
            int age = ThreadLocalRandom.current().nextInt(ageMin, ageMax);
            c.setAge(age);
            clientRespository.save(c);
            stats.addValue(age);
        }

    }
}