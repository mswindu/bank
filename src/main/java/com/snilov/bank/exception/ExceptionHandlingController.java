package com.snilov.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            BindingResultException.class,
            ThereIsNoSuchAccountException.class
    })
    public ResponseEntity<Object> applicationHandleException(Exception ex) {
        if (ex instanceof BindingResultException) {
            return new ResponseEntity<>(new ResponseException("Invalid parameters specified.", ((BindingResultException) ex).getErrors()), HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ThereIsNoSuchAccountException) {
            return new ResponseEntity<>(new ResponseException(ex.getMessage()), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }

}
