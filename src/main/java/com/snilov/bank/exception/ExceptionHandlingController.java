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
    public ResponseEntity<AwesomeException> invalidInput(ValidationException ex) {
        String result = ex.getMessage();
        return new ResponseEntity<AwesomeException>(new AwesomeException(result), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ThereIsNoSuchAccountException.class)
    protected ResponseEntity<AwesomeException> handleThereIsNoSuchAccountException() {
        return new ResponseEntity<>(new AwesomeException("There is no such account"), HttpStatus.NOT_FOUND);
    }

    private static class AwesomeException {
        private String message;

        AwesomeException(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
