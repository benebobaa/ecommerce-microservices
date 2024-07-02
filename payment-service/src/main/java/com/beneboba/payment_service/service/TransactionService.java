package com.beneboba.payment_service.service;

import com.beneboba.payment_service.exception.OrderNotFound;
import com.beneboba.payment_service.model.Balance;
import com.beneboba.payment_service.model.Transaction;
import com.beneboba.payment_service.model.event.OrderEvent;
import com.beneboba.payment_service.producer.KafkaTransactionProducer;
import com.beneboba.payment_service.repository.BalanceRepository;
import com.beneboba.payment_service.repository.TransactionRepository;
import com.beneboba.payment_service.util.Helper;
import com.beneboba.payment_service.util.ObjectConverter;
import com.beneboba.payment_service.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final BalanceRepository balanceRepository;

    private final ValidationService validationService;

    private final KafkaTransactionProducer kafkaTransactionProducer;

    private final ObjectConverter objectConverter;

    public Flux<Transaction> getAllTransactions() {
        log.info("getAllTransactions");

        return transactionRepository.findAll();
    }

    public Mono<Void> createTransaction(String orderPaymentEvent) {
        log.info("createTransaction -> {}", orderPaymentEvent);

        OrderEvent orderEventObj = objectConverter
                .convertStringToObject(orderPaymentEvent, OrderEvent.class);

        return balanceRepository.findByCustomerId(orderEventObj.getCustomerId())
                .switchIfEmpty(Mono.error(new RuntimeException("Balance not found for customer: " + orderEventObj.getCustomerId())))
                .flatMap(balance -> {
                    String mode = Helper.classifyPaymentMethod(orderEventObj.getPaymentMethod());
                    String status;
                    Mono<Balance> balanceUpdate;

                    if (balance.getBalance() >= orderEventObj.getTotalAmount()) {
                        log.info("Customer Balance -> {}", balance);

                        status = "SUCCESS";
                        balance.setBalance(balance.getBalance() - orderEventObj.getTotalAmount());
                        orderEventObj.setPaymentStatus(true);
                        balanceUpdate = balanceRepository.save(balance);
                    } else {
                        log.info("Insufficient balance for customer: {}", balance);

                        orderEventObj.setProductStatus(false);
                        status = "FAILED";
                        balanceUpdate = Mono.just(balance);
                    }

                    Transaction transaction = new Transaction(
                            orderEventObj.getOrderId(),
                            orderEventObj.getTotalAmount(),
                            mode,
                            status,
                            Helper.generateReferenceNumber()
                    );
                    return balanceUpdate
                            .then(transactionRepository.save(transaction))
                            .doOnSuccess(savedTransaction -> {
                                        log.info("Transaction processed: {} for orderId: {}",
                                                status, orderEventObj.getOrderId());
                                        kafkaTransactionProducer
                                                .sendOrderPaymentSuccess(
                                                        objectConverter.convertObjectToString(orderEventObj));
                                    }
                                    );
                })
                .onErrorResume(error -> {
                    log.error("Error processing transaction: {}", error.getMessage());
                    kafkaTransactionProducer
                            .sendOrderPaymentFailed(objectConverter.convertObjectToString(orderEventObj));
                    return Mono.empty();
                })
                .then();
    }
}
