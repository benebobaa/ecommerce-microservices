package com.beneboba.cart_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${service.api.product.url}")
    private String productUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(productUrl)
                .build();
    }
}