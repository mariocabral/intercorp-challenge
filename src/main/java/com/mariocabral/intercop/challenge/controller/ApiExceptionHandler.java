package com.mariocabral.intercop.challenge.controller;

import com.mariocabral.intercop.challenge.exception.GenderCalculatorException;
import com.mariocabral.intercop.challenge.exception.InvalidClientException;
import com.mariocabral.intercop.challenge.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseBody
    @ExceptionHandler(InvalidClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage deviceNotFoundHandler(InvalidClientException ex) {
        ErrorMessage error = new ErrorMessage(new Date(),HttpStatus.BAD_REQUEST.value(), "Invalid Client", ex.getMessage());
        return error;
    }

    @ResponseBody
    @ExceptionHandler(GenderCalculatorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorMessage deviceValidationHandler(GenderCalculatorException ex) {
        ErrorMessage error = new ErrorMessage(new Date(),HttpStatus.BAD_REQUEST.value(), "Fail gender detector", ex.getMessage());
        return error;
    }
}
