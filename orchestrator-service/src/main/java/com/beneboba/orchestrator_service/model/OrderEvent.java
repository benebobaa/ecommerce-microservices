package com.beneboba.orchestrator_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OrderEvent {

    private Long orderId;

    private Long customerId;

    private boolean productStatus;

    private boolean paymentStatus;

    private String paymentMethod;

    private Float totalAmount;

    private List<ProductEvent> products;
}
