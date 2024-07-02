package com.beneboba.order_service.util;

import com.beneboba.order_service.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    public <T> Mono<T> validate(T request) {
        return Mono.fromCallable(() -> {
            Set<ConstraintViolation<T>> violations = validator.validate(request);

            log.info("{} validate -> {}", getClass().getSimpleName(), violations);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            return request;
        });
    }
}