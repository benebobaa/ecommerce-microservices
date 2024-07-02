package com.beneboba.order_service.handler;

import com.beneboba.order_service.exception.ValidationException;
import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.repository.OrderRepository;
import com.beneboba.order_service.util.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @Mock
    private ValidationService validationService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderHandler orderHandler;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = new Order(1L, 1L, "Billing Address",
                "Shipping Address", "PENDING",
                "CREDIT_CARD", 100.0f, LocalDate.now());
    }

    @Test
    void createOrder_Success() {
        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(sampleOrder));

        when(validationService.validate(any(Order.class))).thenReturn(Mono.just(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(sampleOrder));

        Mono<ServerResponse> responseMono = orderHandler.createOrder(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void createOrder_ValidationFailure() {
        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(sampleOrder));

        when(validationService.validate(any(Order.class)))
                .thenReturn(Mono.error(new ValidationException(List.of("Validation error"))));

        Mono<ServerResponse> responseMono = orderHandler.createOrder(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void createOrder_UnexpectedError() {
        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(sampleOrder));

        when(validationService.validate(any(Order.class))).thenReturn(Mono.just(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        Mono<ServerResponse> responseMono = orderHandler.createOrder(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is5xxServerError())
                .verifyComplete();
    }

    @Test
    void getAllOrders_Success() {
        ServerRequest request = MockServerRequest.builder().build();

        List<Order> orders = Arrays.asList(sampleOrder, sampleOrder);
        when(orderRepository.findAll()).thenReturn(Flux.fromIterable(orders));

        Mono<ServerResponse> responseMono = orderHandler.getAllOrders(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void getAllOrders_EmptyList() {
        ServerRequest request = MockServerRequest.builder().build();

        when(orderRepository.findAll()).thenReturn(Flux.empty());

        Mono<ServerResponse> responseMono = orderHandler.getAllOrders(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void getAllOrders_UnexpectedError() {
        ServerRequest request = MockServerRequest.builder().build();

        when(orderRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Unexpected error")));

        Mono<ServerResponse> responseMono = orderHandler.getAllOrders(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is5xxServerError())
                .verifyComplete();
    }
}