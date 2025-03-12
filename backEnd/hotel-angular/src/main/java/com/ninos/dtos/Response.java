package com.ninos.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninos.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // ignore any field is null
public class Response {

    // generic
    private int status;
    private String message;

    // for login
    private String token;
    private UserRole role;
    private Boolean active;
    private String expirationTime;

    // user data output
    private UserDTO user;
    private List<UserDTO> users;

    // booking data output
    private BookingDTO booking;
    private List<BookingDTO> bookings;

    // room data output
    private RoomDTO room;
    private List<RoomDTO> rooms;

    // payment data output
    private String transactionId;
    private PaymentDTO payment;
    private List<PaymentDTO> payments;

    // notification data output
    private NotificationDTO notification;
    private List<NotificationDTO> notifications;

    private final LocalDateTime timestamp = LocalDateTime.now();




}
