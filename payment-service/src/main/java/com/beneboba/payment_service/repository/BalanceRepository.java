package com.beneboba.payment_service.repository;

import com.beneboba.payment_service.model.Balance;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface BalanceRepository extends R2dbcRepository<Balance,Long> {
    Mono<Balance> findByCustomerId(Long customerId);
}
