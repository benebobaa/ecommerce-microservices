package com.beneboba.cart_service.exception;

public class CartNotFound extends Exception {

    public CartNotFound(String message) {
        super(message);
    }

    public CartNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public CartNotFound(Throwable cause) {
        super(cause);
    }

    public CartNotFound() {
        super();
    }

}
