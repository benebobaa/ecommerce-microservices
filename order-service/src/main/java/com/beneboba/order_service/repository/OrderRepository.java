package com.beneboba.order_service.repository;

import com.beneboba.order_service.model.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderRepository extends R2dbcRepository<Order, Long> {
}
