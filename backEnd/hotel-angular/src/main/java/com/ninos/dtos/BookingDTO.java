package com.ninos.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninos.enums.BookingStatus;
import com.ninos.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // Ignore the null value in json output
@JsonIgnoreProperties(ignoreUnknown = true) // compare the dto variable with entity variable and ignore the extra variable in dto in json output
public class BookingDTO {

    private Long id;
    private UserDTO user;
    private RoomDTO room;
    private Long roomId;
    private PaymentStatus paymentStatus;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;
    private String bookingReference;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;

}
