package com.beneboba.product_service.service;


import com.beneboba.product_service.exception.ProductNotFound;
import com.beneboba.product_service.model.BaseResponse;
import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.model.event.OrderEvent;
import com.beneboba.product_service.model.event.ProductEvent;
import com.beneboba.product_service.producer.KafkaProductProducer;
import com.beneboba.product_service.repository.ProductRepository;
import com.beneboba.product_service.util.ObjectConverter;
import com.beneboba.product_service.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ValidationService validationService;

    private final ProductRepository productRepository;

    private final ObjectConverter objectConverter;

    private final KafkaProductProducer kafkaProductProducer;

    public Mono<BaseResponse<Product>> createProduct(Product request) {
        log.info("createProduct -> " + request);

        validationService.validate(request);

        Mono<Product> product = productRepository.save(request);

        return product.map(savedProduct -> new BaseResponse<>(savedProduct, null));
    }

    public Mono<BaseResponse<Product>> updateProduct(Product request) {
        log.info("updateProduct -> " + request);

        validationService.validate(request);

        Mono<Product> product = productRepository.save(request);

        return product.map(savedProduct -> new BaseResponse<>(savedProduct, null));
    }

    public Flux<Product> getAllProducts() {
        log.info("getAllProducts");

        return productRepository.findAll();

    }

    public Mono<BaseResponse<Product>> getProductById(Long id){
        log.info("getProductById -> " + id);

        return productRepository.findById(id)
                .map(product -> new BaseResponse<>(product, null))
                .switchIfEmpty(Mono.error(
                        new ProductNotFound(
                                String.format("Product not found. ID: %s", id)
                        )
                ));
    }

    public Mono<Void> deleteProduct(Long id) {
        log.info("deleteProduct -> " + id);

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ProductNotFound(
                                String.format("Product not found. ID: %s", id)
                        )
                ))
                .flatMap(product -> productRepository.deleteById(id));
    }

    // Create validate and reduce quantity method
    public Mono<Void> validateAndReduceQuantity(String orderEventString) {
        log.info("Validating and reducing quantity for order: {}", orderEventString);

        return Mono.fromCallable(() -> objectConverter.convertStringToObject(orderEventString, OrderEvent.class))
                .flatMap(orderEvent -> Flux.fromIterable(orderEvent.getProducts())
                        .flatMap(productEvent -> productRepository.findById(productEvent.getProductId())
                                .switchIfEmpty(Mono.defer(() -> {
                                    log.warn("Product not found: {}", productEvent.getProductId());
                                    return Mono.empty();
                                }))
                                .flatMap(product -> {
                                    if (product.getStockQuantity() < productEvent.getQuantity()) {
                                        log.warn("Insufficient stock for product: {}", product.getId());
                                        return Mono.empty();
                                    }
                                    log.info("Processing product: ID = {}, Current Stock = {}, Requested Quantity = {}",
                                            product.getId(), product.getStockQuantity(), productEvent.getQuantity());
                                    product.setStockQuantity(product.getStockQuantity() - productEvent.getQuantity());
                                    productEvent.setPrice(product.getPrice());
                                    return productRepository.save(product);
                                })
                        )
                        .collectList()
                        .map(processedProducts -> {
                            boolean allProductsValid = processedProducts.size() == orderEvent.getProducts().size();
                            orderEvent.setProductStatus(allProductsValid);
                            return orderEvent;
                        })
                )
                .flatMap(processedOrderEvent -> Mono.fromRunnable(() -> {
                    String updatedOrderEvent = objectConverter.convertObjectToString(processedOrderEvent);
                    if (processedOrderEvent.isProductStatus()) {
                        kafkaProductProducer.sendOrderProductCreated(updatedOrderEvent);
                        log.info("Sent order product created message");
                    } else {
                        kafkaProductProducer.sendOrderProductRevert(updatedOrderEvent);
                        log.info("Sent order product revert message");
                    }
                }))
                .doOnError(e -> log.error("Error processing order: ", e))
                .then();
    }
}
