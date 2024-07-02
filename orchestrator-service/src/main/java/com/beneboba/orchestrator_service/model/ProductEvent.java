package com.beneboba.orchestrator_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductEvent {

        private Long productId;

        private Integer quantity;

        private Float price;
}
