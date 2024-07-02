package com.beneboba.order_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("order_items")
public class OrderItem {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    private Long productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private float price;

    @Min(1)
    private Integer quantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long orderId;
}
