package com.beneboba.cart_service.client;

import com.beneboba.cart_service.exception.ProductNotFound;
import com.beneboba.cart_service.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductClient {

    private final WebClient webClient;

    public Mono<Product> getProductById(Long id) {
        log.info("getProductById -> " + id);

        Mono<Product> productResponse = webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        log.error("getProductById -> {}", "Product not found. ID: " + id);
                        return Mono.error(new ProductNotFound("Product not found. ID: " + id));
                    }
                    log.error("getProductById -> {}", "Client error " + clientResponse.statusCode());
                    return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Server error")))
                .bodyToMono(Product.class)
                .doOnNext(product -> log.info("getProductById -> " + product))
                .doOnError(throwable -> log.error("getProductById error -> " + throwable.getMessage()));

        return productResponse;
    }

}
