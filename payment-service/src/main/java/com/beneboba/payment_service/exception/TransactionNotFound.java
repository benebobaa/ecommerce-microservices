package com.beneboba.payment_service.exception;

public class TransactionNotFound extends Exception{
    public TransactionNotFound(String message) {
        super(message);
    }
}
