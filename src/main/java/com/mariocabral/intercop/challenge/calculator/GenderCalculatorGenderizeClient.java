package com.mariocabral.intercop.challenge.calculator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariocabral.intercop.challenge.exception.GenderCalculatorException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class GenderCalculatorGenderizeClient implements GenderCalculator{

    public static String URL_GENDERIZE = "https://api.genderize.io?name=";

    @Override
    public Gender calculateGender(String name) {
        URL url = null;
        try {
            url = new URL(URL_GENDERIZE+name);
        } catch (MalformedURLException e) {
            throw new GenderCalculatorException("Invalid URL used to connect with genderize", e);
        }
        HttpURLConnection conn = null;
        String gender = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() != 200) {
                throw new GenderCalculatorException("Error: " + conn.getResponseCode());
            }
            InputStreamReader input = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(input);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(reader);
            gender = jsonNode.get("gender").asText();
            log.debug("Gender detected : " + gender + " for the name: " + name );
            conn.disconnect();
        } catch (IOException e) {
            throw new GenderCalculatorException("Invalid response from genderize", e);
        }

        return Gender.valueOf(gender.toUpperCase());
    }
}
