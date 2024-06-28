package com.beneboba.cart_service.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
@Slf4j
public class ProductRepository {


    @Value("${service.api.product.url}")

    private final WebClient webClient;


    public ProductRepository(WebClient webClient) {
        this.webClient = webClient;
    }
}
