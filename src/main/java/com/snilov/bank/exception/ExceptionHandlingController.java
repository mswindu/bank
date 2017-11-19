package com.snilov.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseException> invalidInput(ValidationException ex) {
        return new ResponseEntity<ResponseException>(new ResponseException(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ThereIsNoSuchAccountException.class)
    protected ResponseEntity<ResponseException> handleThereIsNoSuchAccountException(ThereIsNoSuchAccountException ex) {
        return new ResponseEntity<>(new ResponseException(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}
