package com.snilov.bank.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class AccountExceptionHandler extends ResponseEntityExceptionHandler {

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