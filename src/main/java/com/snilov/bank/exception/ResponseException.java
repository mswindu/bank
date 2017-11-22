package com.snilov.bank.exception;

import java.util.Map;

public class ResponseException {
    private String message;
    private Map<String, String> errors;

    ResponseException(String message) {
        this.message = message;
    }

    ResponseException(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
