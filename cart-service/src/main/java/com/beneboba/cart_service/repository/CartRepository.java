package com.beneboba.cart_service.repository;

import com.beneboba.cart_service.model.Cart;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CartRepository extends R2dbcRepository<Cart, Long> {
}
