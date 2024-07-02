package com.beneboba.product_service.consumer;

import com.beneboba.product_service.repository.ProductRepository;
import com.beneboba.product_service.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProductConsumer {

    private final ProductService productService;

    @KafkaListener(
            topics = "product-check",
            groupId = "bene-group"
    )
    public void productCheckEvent(String orderEvent) {
        log.info("handleUpdateProductRequest -> {}", orderEvent);

        productService.validateAndReduceQuantity(orderEvent)
                .subscribe();
    }
}
