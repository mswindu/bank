package com.snilov.bank.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));

        Map<String, String> errors = new HashMap<>();
        errors.put("description", builder.toString());
        return new ResponseEntity<>(new ResponseException(ex.getMessage(), errors), HttpStatus.METHOD_NOT_ALLOWED);
    }

}
