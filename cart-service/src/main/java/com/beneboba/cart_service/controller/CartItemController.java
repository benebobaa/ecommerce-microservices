package com.beneboba.cart_service.controller;

import com.beneboba.cart_service.model.BaseResponse;
import com.beneboba.cart_service.model.CartItem;
import com.beneboba.cart_service.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
@Slf4j
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BaseResponse<CartItem>> createCartItem(@RequestBody CartItem cartItem) {
        log.info("createCartItem -> " + cartItem);

        return cartItemService.createCartItem(cartItem);
    }
}
