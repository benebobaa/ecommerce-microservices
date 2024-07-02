package com.beneboba.payment_service.exception;

public class OrderNotFound extends Exception {
    public OrderNotFound(String message) {
        super(message);
    }
}
