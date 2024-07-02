package com.beneboba.orchestrator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrchestratorService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "order-events",
            groupId = "bene-group"
    )
    public void handleOrderEvent(String orderEvent) throws JsonProcessingException {
        log.info("Checking product availability for order: {}", orderEvent);

        kafkaTemplate.send("product-check", orderEvent);
    }

    @KafkaListener(
            topics = "payment-events",
            groupId = "bene-group"
    )
    public void handlePaymentEvent(String orderPaymentEvent) throws JsonProcessingException {
        log.info("Received payment event: {}", orderPaymentEvent);

        kafkaTemplate.send("payment-initiate", orderPaymentEvent);
    }

    @KafkaListener(
            topics = "payment-failed",
            groupId = "bene-group"
    )
    public void handlePaymentFailed(String orderPaymentEvent) throws JsonProcessingException {
        log.info("Received payment failed event: {}", orderPaymentEvent);

        kafkaTemplate.send("order-product-payment-failed", orderPaymentEvent);
    }


    @KafkaListener(
            topics = "payment-success",
            groupId = "bene-group"
    )
    public void handlePaymentSuccess(String orderPaymentEvent) throws JsonProcessingException {
        log.info("Received payment success event: {}", orderPaymentEvent);

        kafkaTemplate.send("order-product-payment-complete", orderPaymentEvent);
    }

//    private void checkProductAvailability(String orderEvent) {
//        log.info("Checking product availability for order: {}", orderEvent);
//
//        // Send message to Product Service
//
//        kafkaTemplate.send("product-check", orderEvent);
//    }

//    @KafkaListener(topics = "product-response")
//    public void handleProductResponse(ProductEvent productEvent) {
//        log.info("Received product event: {}", productEvent);
//        if (productEvent.isAvailable()) {
//            initiatePayment(productEvent.getOrderId());
//        } else {
//            cancelOrder(productEvent.getOrderId());
//        }
//    }
//
//    private void initiatePayment(String orderId) {
//        log.info("Initiating payment for order: {}", orderId);
//        // Send message to Payment Service
//        kafkaTemplate.send("payment-initiate", new PaymentEvent(orderId));
//    }
//
//    @KafkaListener(topics = "payment-response")
//    public void handlePaymentResponse(PaymentEvent paymentEvent) {
//        log.info("Received payment event: {}", paymentEvent);
//        if (paymentEvent.isSuccessful()) {
//            confirmOrder(paymentEvent.getOrderId());
//        } else {
//            cancelOrder(paymentEvent.getOrderId());
//        }
//    }
//
//    private void confirmOrder(String orderId) {
//        // Send confirmation to Order Service
//        kafkaTemplate.send("order-confirm", orderId);
//    }
//
//    private void cancelOrder(String orderId) {
//        // Send cancellation to Order Service
//        kafkaTemplate.send("order-cancel", orderId);
//    }
}