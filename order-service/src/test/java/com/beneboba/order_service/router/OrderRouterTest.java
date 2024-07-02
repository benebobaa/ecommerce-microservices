package com.beneboba.order_service.router;

import com.beneboba.order_service.handler.OrderHandler;
import com.beneboba.order_service.model.dto.BaseResponse;
import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.repository.OrderItemRepository;
import com.beneboba.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(OrderRouter.class)
@AutoConfigureWebTestClient
public class OrderRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderHandler orderHandler;

    @MockBean
    private ConnectionFactoryInitializer initializer;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderItemRepository orderItemRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setCustomerId(1L);
        order.setBillingAddress("Billing");
        order.setShippingAddress("Shipping");
        order.setOrderStatus("Pending");
        order.setPaymentMethod("QRIS");
        order.setTotalAmount(20.0f);
    }

    @Test
    void testCreateOrder() {
        when(orderHandler.createOrder(any()))
                .thenReturn(ServerResponse.status(201)
                        .bodyValue(order));

        webTestClient.post()
                .uri("/api/orders")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(order), Order.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Order.class)
                .isEqualTo(order);
    }

    @Test
    void testCreateOrder_Invalid() {
        when(orderHandler.createOrder(any()))
                .thenReturn(ServerResponse.status(400)
                        .bodyValue(new BaseResponse<>(null, "Invalid order", List.of("Validation error"))));

        webTestClient.post()
                .uri("/api/orders")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(new Order()), Order.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(BaseResponse.class);
    }

    @Test
    void testGetAllOrders() {
        when(orderHandler.getAllOrders(any()))
                .thenReturn(ServerResponse.ok()
                        .bodyValue(List.of(order)));

        webTestClient.get()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(1)
                .contains(order);
    }

    @Test
    void testGetAllOrders_Empty() {
        when(orderHandler.getAllOrders(any()))
                .thenReturn(ServerResponse.ok()
                        .bodyValue(List.of()));

        webTestClient.get()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(0);
    }

//    @Test
//    void testGetOrderById() {
//        when(orderHandler.getOrderById(any()))
//                .thenReturn(ServerResponse.ok()
//                        .bodyValue(order));
//
//        webTestClient.get()
//                .uri("/api/orders/1")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Order.class)
//                .isEqualTo(order);
//    }

//    @Test
//    void testGetOrderById_NotFound() {
//        when(orderHandler.getOrderById(any()))
//                .thenReturn(ServerResponse.notFound().build());
//
//        webTestClient.get()
//                .uri("/api/orders/99")
//                .exchange()
//                .expectStatus().isNotFound();
//    }
//
//    @Test
//    void testDeleteOrder() {
//        when(orderHandler.deleteOrder(any()))
//                .thenReturn(ServerResponse.noContent().build());
//
//        webTestClient.delete()
//                .uri("/api/orders/1")
//                .exchange()
//                .expectStatus().isNoContent();
//    }
//
//    @Test
//    void testDeleteOrder_NotFound() {
//        when(orderHandler.deleteOrder(any()))
//                .thenReturn(ServerResponse.notFound().build());
//
//        webTestClient.delete()
//                .uri("/api/orders/99")
//                .exchange()
//                .expectStatus().isNotFound();
//    }
}
