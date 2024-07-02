package com.beneboba.product_service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProductProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrderProductCreated(String orderEvent) {
        log.info("Sending product event -> {}", orderEvent);

        kafkaTemplate.send("order-product-created", orderEvent);
    }


    public void sendOrderProductRevert(String orderEvent) {
        log.info("Sending product revert -> {}", orderEvent);

        kafkaTemplate.send("order-product-revert", orderEvent);
    }
}
