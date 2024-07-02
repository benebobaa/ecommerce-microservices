package com.beneboba.order_service.config;

import com.beneboba.order_service.model.event.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class GlobalHashMap {

    @Bean
    public Map<Long, OrderEvent> productOrderCache() {
        return new ConcurrentHashMap<>();
    }
}
