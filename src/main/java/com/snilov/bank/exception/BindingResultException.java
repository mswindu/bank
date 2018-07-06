package com.snilov.bank.exception;

import java.util.Map;
import java.util.Set;

/**
 * Exception thrown if there were any validation errors.
 */
public class BindingResultException extends RuntimeException {

    private final Map<String, String> errors;

    public BindingResultException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        Set keys = errors.keySet();
        StringBuilder result = new StringBuilder("Error parameters: ");
        for (Object key: keys) {
            result.append(key.toString()).append(", ");
        }
        return String.valueOf(result);
    }
}
