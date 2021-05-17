package com.mariocabral.intercop.challenge.exception;

public class GenderCalculatorException extends RuntimeException{
    public GenderCalculatorException(String message) {
        super(message);
    }

    public GenderCalculatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
