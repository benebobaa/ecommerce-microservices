package com.beneboba.payment_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("customer_balance")
public class Balance {

    @Id
    private Long id;

    private Long customerId;

    private float balance;
}
