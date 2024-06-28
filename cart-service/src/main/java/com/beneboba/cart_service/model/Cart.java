package com.beneboba.cart_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("carts")
public class Cart {

    @Id
    @JsonIgnore
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String status;

    @NotNull
    @Min(1)
    private float totalPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
