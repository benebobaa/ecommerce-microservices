package com.beneboba.product_service.controller;

import com.beneboba.product_service.Helper;
import com.beneboba.product_service.exception.ProductNotFound;
import com.beneboba.product_service.model.BaseResponse;
import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.repository.ProductRepository;
import com.beneboba.product_service.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = ProductController.class)
@AutoConfigureWebTestClient
public class ErrorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ErrorController errorController;

    @MockBean
    private ConnectionFactoryInitializer initializer;

    private final String url = "/api/products";

    @Test
    void testConstraintViolationException() throws JsonProcessingException {
        Product badProduct = Helper.newProduct();
        badProduct.setName(null);

        doThrow(new ConstraintViolationException("Validation error", null))
                .when(productService).createProduct(badProduct);

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(badProduct))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }


//    @Test
//    void testProductNotFoundException() {
//        Long id = 10L;
//        doThrow(new ProductNotFound("Product not found", null))
//                .when(productService).getProductById(id);
//
////        when(productService.getProductById(id)).thenThrow(new ProductNotFound("Product not found"));
//
//        String urlGetById = url + "/" + id;
//
//        webTestClient.get().uri(urlGetById)
//                .exchange()
//                .expectStatus()
//                .isNotFound()
//                .expectBody(BaseResponse.class)
//                .value(response -> {
//                    assertNotNull(response.getErrors());
//                });
//    }

    @Test
    void testProductNotFoundException() {
        // Arrange
        Long id = 10L;
        String errorMessage = "Product not found";

        // Mock ProductService to throw ProductNotFound
        when(productService.getProductById(id))
                .thenReturn(Mono.error(new ProductNotFound(errorMessage)));

        when(errorController.productNotFoundException(any(ProductNotFound.class)))
                .thenReturn(Mono.just(new BaseResponse<>(null, errorMessage)));


        String urlGetById = url + "/" + id;

        // Act and Assert
        webTestClient.get()
                .uri(urlGetById)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}
