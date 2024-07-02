package com.beneboba.order_service;

import com.beneboba.order_service.exception.ValidationException;
import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.util.ValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private ValidationService validationService;

    @Test
    void validateValidObject() {
        Order order = new Order(1L, 1L, "billing",
                "shipping", "Pending", "QRIS", 20.0f,
                LocalDate.now());

        when(validator.validate(order)).thenReturn(new HashSet<>());

        StepVerifier.create(validationService.validate(order))
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    void validateInvalidObject() {
        Order order = new Order(1L, null, "billing",
                "shipping", "Pending", "QRIS", 0,
                LocalDate.now());

        Set<ConstraintViolation<Order>> violations = new HashSet<>();

        violations.add(mock(ConstraintViolation.class));

        when(validator.validate(order)).thenReturn(violations);
        when(violations.iterator().next().getMessage()).thenReturn("customerId cant null");

        StepVerifier.create(validationService.validate(order))
                .expectError()
                .verify();

        verify(validator, times(1)).validate(order);
    }
}