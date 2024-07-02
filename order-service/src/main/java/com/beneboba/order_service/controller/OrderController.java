package com.beneboba.order_service.controller;

import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.model.dto.BaseResponse;
import com.beneboba.order_service.model.dto.OrderRequest;
import com.beneboba.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.PROCESSING)
    public Mono<BaseResponse<Order>> createOrder(@RequestBody OrderRequest request) {
        log.info("createOrder -> {}", request);

        return orderService.createOrder(request)
                .map(order -> new BaseResponse<>(order, null));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<BaseResponse<List<Order>>> getAllOrders() {
        log.info("getAllOrders");

        return orderService.getAllOrders()
                .collectList()
                .map(orders -> new BaseResponse<>(orders, null));
    }
}
