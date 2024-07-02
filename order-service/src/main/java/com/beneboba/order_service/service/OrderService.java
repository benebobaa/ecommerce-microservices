package com.beneboba.order_service.service;

import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.model.OrderItem;
import com.beneboba.order_service.model.dto.OrderRequest;
import com.beneboba.order_service.model.event.OrderEvent;
import com.beneboba.order_service.model.event.ProductEvent;
import com.beneboba.order_service.producer.KafkaOrderProducer;
import com.beneboba.order_service.repository.OrderItemRepository;
import com.beneboba.order_service.repository.OrderRepository;
import com.beneboba.order_service.util.ObjectConverter;
import com.beneboba.order_service.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {


    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ValidationService validationService;

    private final KafkaOrderProducer kafkaOrderProducer;

    private final ObjectConverter objectConverter;

    @Transactional
    public Mono<Order> createOrder(OrderRequest request) {
        log.info("createOrder -> {}", request);

        return validationService.validate(request)
                .flatMap(validatedRequest -> {
                    validatedRequest.getOrder().setOrderStatus("PROCESSING");
                    return orderRepository.save(validatedRequest.getOrder());
                })
                .flatMap(order -> {
                    List<OrderItem> orderItems = request.getOrderItems();
                    orderItems.forEach(orderItem -> orderItem.setOrderId(order.getId()));

                    return orderItemRepository.saveAll(orderItems)
                            .collectList()
                            .thenReturn(order);
                })
                .doOnSuccess(order -> {
                    log.info("Order created successfully: {}", order);
                    List<ProductEvent> productEvents = request.getOrderItems().stream()
                            .map(orderItem -> new ProductEvent(orderItem.getProductId(), orderItem.getQuantity(), 0f))
                            .toList();

                    OrderEvent orderEvent = new OrderEvent(
                            order.getId(),
                            order.getCustomerId(),
                            false,false,
                            order.getPaymentMethod(),
                            null,
                            productEvents
                    );

                    log.info("Sending order event -> {}", orderEvent);
                    kafkaOrderProducer.sendOrderEvent(
                            objectConverter.convertObjectToString(orderEvent)
                    );
                })
                .doOnError(error -> log.error("Error creating order: {}", error.getMessage()));
    }


    public Flux<Order> getAllOrders() {
        log.info("getAllOrders");
        return orderRepository.findAll();
    }


    @Transactional
    public Mono<Void> updateOrder(String orderEvent) {
        log.info("updateOrder -> {}", orderEvent);

        OrderEvent event = objectConverter.convertStringToObject(orderEvent, OrderEvent.class);

        return orderRepository.findById(event.getOrderId())
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found: " + event.getOrderId())))
                .flatMap(order -> {
                    log.info("Order found: {}", order);

                    if (event.isProductStatus() && event.isPaymentStatus()) {

                        order.setOrderStatus("COMPLETE");
                        log.info("Order completed: {}", order);

                    } else if (!event.isProductStatus() &&
                            order.getOrderStatus().equals("PROCESSING")) {

                        order.setOrderStatus("FAILED");
                        log.info("Order failed: {}", order);

                    } else if (!event.isPaymentStatus() &&
                            order.getOrderStatus().equals("CREATED")){

                        order.setOrderStatus("FAILED ON PAYMENT");
                        log.info("Order failed on payment: {}", order);

                    } else {
                        float totalAmount = calculateTotalAmount(event);
                        order.setTotalAmount(totalAmount);
                        order.setOrderStatus("CREATED");
                        event.setOrderId(order.getId());
                        event.setTotalAmount(totalAmount);
                        event.setPaymentMethod(order.getPaymentMethod());
                        event.setCustomerId(order.getCustomerId());
                        log.info("Order created: {}", order);
                    }

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> {
                                if (event.isProductStatus() &&
                                        order.getOrderStatus().equals("CREATED")) {
                                    return updateOrderItems(event)
                                            .then(sendPaymentEvent(event));
                                }
                                return Mono.empty();
                            });
                })
                .doOnError(error -> log.error("Error updating order: {}", error.getMessage()))
                .then();
    }

    private float calculateTotalAmount(OrderEvent event) {
        return (float) event.getProducts().stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }

    private Flux<OrderItem> updateOrderItems(OrderEvent event) {
        return orderItemRepository.findAllById(Collections.singleton(event.getOrderId()))
                .flatMap(orderItem -> {
                    event.getProducts().forEach(productEvent -> {
                        if (orderItem.getProductId().equals(productEvent.getProductId())) {
                            orderItem.setPrice(productEvent.getPrice());
                        }
                    });
                    return orderItemRepository.save(orderItem);
                })
                .doOnError(error -> log.error("Error updating order items: {}", error.getMessage()));
    }

    private Mono<Void> sendPaymentEvent(OrderEvent paymentEvent) {
        return Mono.fromRunnable(() -> {
            log.info("Order product updated successfully: {}", paymentEvent.getOrderId());
            log.info("Sending payment event order: {}", paymentEvent.getOrderId());
            kafkaOrderProducer.sendPaymentEvent(objectConverter.convertObjectToString(paymentEvent));
        });
    }

}
