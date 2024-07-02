package com.beneboba.payment_service.consumer;

import com.beneboba.payment_service.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
public class KafkaTransactionConsumer {

    @Autowired
    @Qualifier("orderValidationCache")
    private Map<Long, Boolean> orderValidationCache;

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(
            topics = "payment-initiate",
            groupId = "bene-group"
    )
    public Mono<Void> handlePaymentInitiate(String paymentEvent) {
        log.info("handlePaymentInitiate -> {}", paymentEvent);

        return transactionService.createTransaction(paymentEvent);
    }
}
