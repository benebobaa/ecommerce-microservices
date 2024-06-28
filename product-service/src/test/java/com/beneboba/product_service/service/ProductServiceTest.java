package com.beneboba.product_service.service;

import com.beneboba.product_service.Helper;
import com.beneboba.product_service.exception.ProductNotFound;
import com.beneboba.product_service.model.BaseResponse;
import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.repository.ProductRepository;
import com.beneboba.product_service.util.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private ProductService productService;


    @Test
    void testCreateProduct() {
        Product newProduct = Helper.newProduct();
        newProduct.setId(1L);

        doNothing().when(validationService).validate(any());

        when(productRepository.save(any())).thenReturn(Mono.just(newProduct));

        Mono<BaseResponse<Product>> result = productService.createProduct(newProduct);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getData().equals(newProduct))
                .verifyComplete();

        verify(validationService, times(1)).validate(any());
        verify(productRepository, times(1)).save(any());
    }


    @Test
    void testUpdateProduct() {
        Product existingProduct = Helper.newProduct();
        existingProduct.setId(1L);

        doNothing().when(validationService).validate(any());
        when(productRepository.save(any())).thenReturn(Mono.just(existingProduct));

        Mono<BaseResponse<Product>> result = productService.updateProduct(existingProduct);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getData().equals(existingProduct))
                .verifyComplete();

        verify(validationService, times(1)).validate(any());
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(Helper.newProduct());
        productList.add(Helper.newProduct());

        when(productRepository.findAll()).thenReturn(Flux.fromIterable(productList));

        Flux<BaseResponse<Product>> result = productService.getAllProducts();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        Product existingProduct = Helper.newProduct();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepository.deleteById(productId)).thenReturn(Mono.empty());

        Mono<Void> result = productService.deleteProduct(productId);

        StepVerifier.create(result).verifyComplete();

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProduct(productId))
                .expectError(ProductNotFound.class)
                .verify();

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product existingProduct = Helper.newProduct();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Mono.just(existingProduct));

        Mono<BaseResponse<Product>> result = productService.getProductById(productId);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getData().equals(existingProduct))
                .verifyComplete();

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProductById(productId))
                .expectError(ProductNotFound.class)
                .verify();

        verify(productRepository, times(1)).findById(productId);
    }
}
