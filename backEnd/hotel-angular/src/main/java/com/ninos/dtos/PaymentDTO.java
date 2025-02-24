package com.ninos.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninos.enums.PaymentGateway;
import com.ninos.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {

    private Long id;
    private BookingDTO booking;
    private String transactionId;
    private BigDecimal amount;
    private PaymentGateway paymentMethod;
    private LocalDateTime paymentDate;
    private PaymentStatus status;
    private String bookingReference;
    private String failureReason;
    private String  approvalLink;


}
