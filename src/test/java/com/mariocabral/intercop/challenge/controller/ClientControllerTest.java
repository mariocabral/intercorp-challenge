package com.mariocabral.intercop.challenge.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariocabral.intercop.challenge.calculator.GenderCalculatorGenderizeClient;
import com.mariocabral.intercop.challenge.model.Client;
import com.mariocabral.intercop.challenge.model.KPIClient;
import com.mariocabral.intercop.challenge.repository.ClientRespository;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
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

    private static String URL_GENDERIZE = "https://api.genderize.io?name=";


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        clientRespository.deleteAll();
        GenderCalculatorGenderizeClient.URL_GENDERIZE = URL_GENDERIZE;
    }

    @AfterEach
    void tearDown() {
        clientRespository.deleteAll();
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
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        Client result = mapFromJson(content, Client.class);
        assertEquals("TestName", result.getName());
        assertEquals("TestLastName", result.getLastName());
        assertEquals(29, result.getAge());
        assertEquals(birthDate, result.getBirthDate());
        assertNotNull(result.getId());
        assertNotNull(result.getExpectedDateOfDeath());
    }


    @Test
    void newClientInvalidData() throws Exception {
        String uri = "/api/creacliente";
        Client client = new Client();
        client.setAge(29);
        client.setName(null);
        client.setLastName("TestLastName");
        Date birthDate = new Date();
        client.setBirthDate(birthDate);

        String inputJson = mapToJson(client);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Invalid Client, field name required"));
    }

    @Test
    void newClientInvalidGener() throws Exception {
        String uri = "/api/creacliente";
        Client client = new Client();
        client.setAge(29);
        client.setName("Mario");
        client.setLastName("TestLastName");
        Date birthDate = new Date();
        client.setBirthDate(birthDate);

        GenderCalculatorGenderizeClient.URL_GENDERIZE = "invalid_url";

        String inputJson = mapToJson(client);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Invalid URL used to connect with genderize"));
    }



    @Test
    void kpiClient() throws Exception {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        populateDB(stats);
        String uri = "/api/kpideclientes";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
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
            c.setExpectedDateOfDeath(new Date());
            int age = ThreadLocalRandom.current().nextInt(ageMin, ageMax);
            c.setAge(age);
            clientRespository.save(c);
            stats.addValue(age);
        }
    }


    @Test
    void listClientDefault() throws Exception {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        populateDB(stats);
        String uri = "/api/listclientes";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String content = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(content);

        assertEquals(1000, jsonNode.get("totalElements").asInt());
        assertEquals(0, jsonNode.get("number").asInt());
        assertEquals(10, jsonNode.get("size").asInt());
    }
}