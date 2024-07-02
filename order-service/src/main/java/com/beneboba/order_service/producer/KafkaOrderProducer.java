package com.beneboba.order_service.producer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.order-event.topic}")
    private String orderEventTopic;

    public void sendOrderEvent(String orderEvent) {
        log.info("Sending order event -> {}", orderEvent);

        kafkaTemplate.send(orderEventTopic, orderEvent);
    }

    public void sendPaymentEvent(String orderPaymentEvent){
        log.info("Sending payment event -> {}", orderPaymentEvent);

        kafkaTemplate.send("payment-events", orderPaymentEvent);
    }
}
