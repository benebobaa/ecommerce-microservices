package com.beneboba.order_service.exception;

import lombok.Getter;

import java.util.List;


@Getter
public class ValidationException extends Exception {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super("Validation failed");
        this.errors = errors;
    }
}