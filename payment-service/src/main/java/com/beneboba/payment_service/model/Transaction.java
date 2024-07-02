package com.beneboba.payment_service.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("transaction_details")
public class Transaction {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    private Long orderId;

    @Min(1)
    private float amount;

    @NotBlank
    private String mode;

    @NotBlank
    private String status;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$", message = "Reference number must be 8 digits long")
    private String referenceNumber;

    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;


    public Transaction(Long orderId, float amount, String mode, String status, String referenceNumber) {
        this.orderId = orderId;
        this.amount = amount;
        this.mode = mode;
        this.status = status;
        this.referenceNumber = referenceNumber;
    }
}
