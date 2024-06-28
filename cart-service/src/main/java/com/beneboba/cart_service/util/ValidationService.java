package com.beneboba.cart_service.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    public void validate(Object request){
        Set<ConstraintViolation<Object>> validate = validator.validate(request);

        log.info("validate -> " + validate);

        if (!validate.isEmpty()){
            throw new ConstraintViolationException(validate);
        }
    }
}