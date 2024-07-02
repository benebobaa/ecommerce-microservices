package com.beneboba.order_service.model.dto;

import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.model.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @Valid
    @NotNull
    private Order order;

    @Valid
    @NotNull
    private List<OrderItem> orderItems;
}
