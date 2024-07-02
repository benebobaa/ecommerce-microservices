package com.beneboba.payment_service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaTransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrderPaymentSuccess(String orderPaymentEvent) {
        log.info("Sending order payment success: {}", orderPaymentEvent);

        kafkaTemplate.send("payment-success", orderPaymentEvent);
    }

    public void sendOrderPaymentFailed(String request) {
        log.info("Sending order payment failed: {}", request);

        kafkaTemplate.send("payment-failed", request);
    }

}
