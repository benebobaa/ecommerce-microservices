package com.beneboba.product_service.repository;

import com.beneboba.product_service.model.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProductRepository extends R2dbcRepository<Product, Long> {
}
