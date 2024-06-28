package com.beneboba.cart_service.repository;

import com.beneboba.cart_service.model.CartItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CartItemRepository extends R2dbcRepository<CartItem, Long> {
}
