package com.beneboba.cart_service.service;

import com.beneboba.cart_service.exception.CartNotFound;
import com.beneboba.cart_service.model.BaseResponse;
import com.beneboba.cart_service.model.CartItem;
import com.beneboba.cart_service.repository.CartItemRepository;
import com.beneboba.cart_service.repository.CartRepository;
import com.beneboba.cart_service.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartRepository cartRepository;

    private final ValidationService validationService;

    public Mono<BaseResponse<CartItem>> createCartItem(CartItem request) {
        log.info("createCartItem -> " + request);

        validationService.validate(request);

        // Find the cart by ID and chain the save operation
        return cartRepository.findById(request.getCartId())
                .switchIfEmpty(Mono.error(new CartNotFound(String.format("Cart not found. ID: %s", request.getCartId()))))
                .flatMap(cart -> {
                    log.info("createCartItem -> cart found");
                    Mono<CartItem> cartItem = cartItemRepository.save(request);
                    return cartItem.map(savedCartItem -> new BaseResponse<>(savedCartItem, null));
                });
    }
 }
