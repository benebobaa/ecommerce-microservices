package com.beneboba.order_service.consumer;

import com.beneboba.order_service.repository.OrderRepository;
import com.beneboba.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderConsumer {


    private final OrderRepository orderRepository;

    private final OrderService orderService;

    @Value("${kafka.order-validate.topic}")
    private String orderValidateTopic;

    @KafkaListener(
            topics = "order-product-created",
            groupId = "bene-group"
    )
    public Mono<Void> handleOrderProductCreated(String orderEvent) {
        log.info("handleOrderProductCreated -> {}", orderEvent);

        return orderService.updateOrder(orderEvent);
    }

    @KafkaListener(
            topics = "order-product-revert",
            groupId = "bene-group"
    )
    public Mono<Void> handleOrderProductRevert(String orderEvent) {
        log.info("handleOrderProductRevert -> {}", orderEvent);

        return orderService.updateOrder(orderEvent);
    }

    @KafkaListener(
            topics = "order-product-payment-complete",
            groupId = "bene-group"
    )
    public Mono<Void> handleOrderPaymentSuccess(String paymentEvent) {
        log.info("handleOrderPaymentSuccess -> {}", paymentEvent);

        return orderService.updateOrder(paymentEvent);
    }

    @KafkaListener(
            topics = "order-product-payment-failed",
            groupId = "bene-group"
    )
    public Mono<Void> handleOrderPaymentFailed(String paymentEvent) {
        log.info("handleOrderPaymentFailed -> {}", paymentEvent);

        return orderService.updateOrder(paymentEvent);
    }
}
