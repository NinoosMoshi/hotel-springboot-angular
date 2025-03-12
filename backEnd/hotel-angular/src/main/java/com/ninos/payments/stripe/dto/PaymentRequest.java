package com.ninos.payments.stripe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)  // ignore field that don't have a value
public class PaymentRequest {

    @NotBlank(message = "Booking reference is required")
    private String bookingReference;

    private BigDecimal amount;
    private String transactionId;
    private boolean success;
    private String failureReason;


}

