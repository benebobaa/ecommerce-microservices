package com.beneboba.product_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("products")
public class Product {

    @Id
    @JsonIgnore
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @NotNull
    @Min(1)
    private float price;

    @NotBlank
    @Size(min = 3, max = 255)
    private String category;

    private String description;

    private String imageUrl;

    private Integer stockQuantity;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
