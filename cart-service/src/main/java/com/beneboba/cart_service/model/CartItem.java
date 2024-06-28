package com.beneboba.cart_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("cart_items")
public class CartItem {

    @Id
    @JsonIgnore
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Long cartId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
