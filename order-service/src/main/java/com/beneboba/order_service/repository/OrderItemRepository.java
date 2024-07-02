package com.beneboba.order_service.repository;

import com.beneboba.order_service.model.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {
}
