package com.beneboba.cart_service.service;

import com.beneboba.cart_service.exception.CartNotFound;
import com.beneboba.cart_service.model.BaseResponse;
import com.beneboba.cart_service.model.Cart;
import com.beneboba.cart_service.repository.CartRepository;
import com.beneboba.cart_service.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;

    private final ValidationService validationService;

    public Flux<BaseResponse<Cart>> getAllCarts() {
        log.info("getAllCarts");

        Flux<Cart> carts = cartRepository.findAll();

        log.info("getAllCarts -> " + carts);

        return carts.map(cart -> new BaseResponse<>(cart, null));
    }


    public Mono<BaseResponse<Cart>> createCart(Cart request) {
        log.info("createCart -> " + request);

        validationService.validate(request);

        Mono<Cart> cart = cartRepository.save(request);

        return cart.map(savedCart -> new BaseResponse<>(savedCart, null));
    }

    public Mono<Void> deleteCart(Long id) {
        log.info("deleteCart -> " + id);

        return cartRepository.findById(id)
                .switchIfEmpty(Mono.error(new CartNotFound(String.format("Cart not found. ID: %s", id))))
                .flatMap(cart -> {
                    log.info("deleteCart -> cart found");
                    return cartRepository.delete(cart);
                });
    }
}
