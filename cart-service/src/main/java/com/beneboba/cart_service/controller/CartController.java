package com.beneboba.cart_service.controller;

import com.beneboba.cart_service.model.BaseResponse;
import com.beneboba.cart_service.model.Cart;
import com.beneboba.cart_service.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<BaseResponse<Cart>> getAllCarts() {
        log.info("getAllCarts");

        return cartService.getAllCarts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BaseResponse<Cart>> createCart(@RequestBody Cart cart) {
        log.info("createCart -> " + cart);

        return cartService.createCart(cart);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCart(@PathVariable("id") Long id) {
        log.info("deleteCart -> " + id);

        return cartService.deleteCart(id);
    }
}
