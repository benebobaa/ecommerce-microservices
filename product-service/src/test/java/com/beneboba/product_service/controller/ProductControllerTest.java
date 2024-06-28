package com.beneboba.product_service.controller;


import com.beneboba.product_service.Helper;
import com.beneboba.product_service.model.BaseResponse;
import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.repository.ProductRepository;
import com.beneboba.product_service.service.ProductService;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ProductController.class)
@AutoConfigureWebTestClient
@Slf4j
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ConnectionFactoryInitializer initializer;

    private final String url = "/api/products";

    @Test
    void testCreateProduct() {
        Product newProduct = Helper.newProduct();
        newProduct.setId(1L);

        BaseResponse<Product> response = new BaseResponse<Product>(newProduct, null);

        when(productService.createProduct(any(Product.class))).thenReturn(Mono.just(response));

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newProduct)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testGetAllProducts() {
        Product product1 = Helper.newProduct();
        product1.setId(1L);
        Product product2 = Helper.newProduct();
        product2.setId(2L);

        BaseResponse<Product> response1 = new BaseResponse<Product>(product1, null);
        BaseResponse<Product> response2 = new BaseResponse<Product>(product2, null);

        when(productService.getAllProducts()).thenReturn(Flux.just(response1, response2));

        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BaseResponse.class)
                .hasSize(2);
    }

    @Test
    void testGetProductById() {
        Product newProduct = Helper.newProduct();
        newProduct.setId(1L);

        BaseResponse<Product> response = new BaseResponse<Product>(newProduct, null);

        when(productService.getProductById(anyLong())).thenReturn(Mono.just(response));

        webTestClient.get().uri(url + "/{id}", newProduct.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testUpdateProduct() {
        Product product = Helper.newProduct();
        product.setId(1L);
        BaseResponse<Product> response = new BaseResponse<Product>(product, null);

        when(productService.updateProduct(any(Product.class))).thenReturn(Mono.just(response));

        webTestClient.patch().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(Mono.empty());

        webTestClient.delete().uri(url + "/{id}", productId)
                .exchange()
                .expectStatus().isNoContent();
    }

}
