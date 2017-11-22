package com.snilov.bank.advice;

import com.snilov.bank.exception.BindingResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Workaround class for making JSR-303 annotation validation work for controller method parameters.
 * Check the issue <a href="https://jira.spring.io/browse/DATAREST-593">DATAREST-593</a>
 */
@ControllerAdvice
public class RequestBodyValidationProcessor extends RequestBodyAdviceAdapter {

    private final Validator validator;

    public RequestBodyValidationProcessor(@Qualifier("defaultValidator") @Autowired final Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean supports(final MethodParameter methodParameter, final Type targetType,
                            final Class<? extends HttpMessageConverter<?>> converterType) {
        final Annotation[] parameterAnnotations = methodParameter.getParameterAnnotations();

        for (final Annotation annotation : parameterAnnotations) {
            if (annotation.annotationType().equals(Valid.class)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object afterBodyRead(final Object body, final HttpInputMessage inputMessage, final MethodParameter
            parameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
        final Object obj = super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
        final BindingResult bindingResult = new BeanPropertyBindingResult(obj, obj.getClass().getCanonicalName());
        validator.validate(obj, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            throw new BindingResultException(errors);
        }

        return obj;
    }
}