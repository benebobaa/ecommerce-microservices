package com.beneboba.product_service.controller;

import com.beneboba.product_service.model.BaseResponse;
import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BaseResponse<Product>> createProduct(@RequestBody Product product) {
        log.info("createProduct -> " + product);

        return productService.createProduct(product);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<BaseResponse<List<Product>>> getAllProducts() {
        log.info("getAllProducts");

        return productService.getAllProducts()
                .collectList()
                .map(products -> new BaseResponse<>(products, null));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<BaseResponse<Product>> updateProduct(@RequestBody Product product) {
        log.info("updateProduct -> " + product);

        return productService.updateProduct(product);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BaseResponse<Product>> getProductById(@PathVariable("id") Long id) {
        log.info("getProductById -> " + id);

        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllProducts(@PathVariable("id") Long id) {
        log.info("deleteProduct -> " + id);

        return productService.deleteProduct(id);
    }
}
