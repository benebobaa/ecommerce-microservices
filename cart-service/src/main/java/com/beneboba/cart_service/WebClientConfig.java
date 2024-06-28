package com.beneboba.cart_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${service.api.product.url}")
    private String productServiceUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().baseUrl(productServiceUrl);
    }
}
