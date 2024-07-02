package com.beneboba.cart_service.service;

import com.beneboba.cart_service.client.ProductClient;
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

    private final ProductClient productClient;

    public Mono<BaseResponse<CartItem>> createCartItem(CartItem request) {
        log.info("createCartItem -> " + request);

        validationService.validate(request);

        return productClient.getProductById(request.getProductId())
                .flatMap(product -> cartRepository.findById(request.getCartId())
                        .switchIfEmpty(Mono.error(new CartNotFound(String.format("Cart not found. ID: %s", request.getCartId()))))
                        .flatMap(cart -> {
                            log.info("createCartItem -> cart found");
                            return cartItemRepository.save(request)
                                    .map(savedCartItem -> new BaseResponse<>(savedCartItem, null));
                        }))
                .doOnError(throwable -> log.error("createCartItem error -> " + throwable.getMessage()));
    }
}
