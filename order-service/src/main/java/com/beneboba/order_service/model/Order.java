package com.beneboba.order_service.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Min(0)
    private Long customerId;

    @NotBlank
    @Size(min = 3, max = 255)
    private String billingAddress;

    @NotBlank
    @Size(min = 3, max = 255)
    private String shippingAddress;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderStatus;

    @NotBlank
    @Pattern(regexp = "(?i)^(BCA|BRI|BNI|GOPAY|OVO|DANA)$", message = "Invalid payment method, only support BCA, BRI, BNI, GOPAY, OVO, DANA")
    private String paymentMethod;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private float totalAmount;

    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
}
