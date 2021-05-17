package com.mariocabral.intercop.challenge.calculator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public interface GenderCalculator {

    public enum Gender{MALE, FEMALE};

    public Gender calculateGender(String name) ;
}
