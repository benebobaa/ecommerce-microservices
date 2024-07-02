package com.beneboba.payment_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class GlobalHashMap {

    @Bean
    public Map<Long, Boolean> orderValidationCache() {
        return new ConcurrentHashMap<>();
    }
}
