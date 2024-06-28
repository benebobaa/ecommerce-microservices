package com.beneboba.product_service.service;


import com.beneboba.product_service.exception.ProductNotFound;
import com.beneboba.product_service.model.BaseResponse;
import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.repository.ProductRepository;
import com.beneboba.product_service.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ValidationService validationService;

    private final ProductRepository productRepository;

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

    public Flux<BaseResponse<Product>> getAllProducts() {
        log.info("getAllProducts");

        Flux<Product> products = productRepository.findAll();

        log.info("getAllProducts -> " + products);

        return products.map(product -> new BaseResponse<>(product, null));
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
}
