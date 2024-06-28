package com.beneboba.product_service;

import com.beneboba.product_service.model.Product;

import java.time.Instant;

public class Helper {

    public static Product newProduct(){
        return new Product(null,
                "test", 2f,
                "test", "test",
                "test", 1,
                 Instant.now(), Instant.now());
    }
}
