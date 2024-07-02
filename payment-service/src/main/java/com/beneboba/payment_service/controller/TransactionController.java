package com.beneboba.payment_service.controller;

import com.beneboba.payment_service.model.event.BaseResponse;
import com.beneboba.payment_service.model.Transaction;
import com.beneboba.payment_service.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;


//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<BaseResponse<Transaction>> createTransaction(@RequestBody Transaction transaction) {
//        log.info("createTransaction -> {}", transaction);
//
//        return transactionService.saveTransaction(transaction)
//                .map(savedTransaction -> new BaseResponse<>(savedTransaction, null))
//                .doOnSuccess(response -> log.info("createTransaction -> {}", response))
//                .doOnError(error -> log.error("createTransaction -> {}", error));
//    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<BaseResponse<List<Transaction>>> getAllTransactions() {
        log.info("getAllTransactions");

        return transactionService.getAllTransactions()
                .collectList()
                .map(
                        transactions -> new BaseResponse<>(transactions, null)
                )
                .doOnSuccess(response -> log.info("getAllTransactions -> {}", response))
                .doOnError(error -> log.error("getAllTransactions -> {}", error));
    }
}
