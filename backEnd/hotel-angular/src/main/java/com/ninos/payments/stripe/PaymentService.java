package com.ninos.payments.stripe;

import com.ninos.dtos.NotificationDTO;
import com.ninos.dtos.Response;
import com.ninos.entities.Booking;
import com.ninos.entities.PaymentEntity;
import com.ninos.enums.NotificationType;
import com.ninos.enums.PaymentGateway;
import com.ninos.enums.PaymentStatus;
import com.ninos.exceptions.NotFoundException;
import com.ninos.notification.NotificationService;
import com.ninos.payments.stripe.dto.PaymentRequest;
import com.ninos.repositories.BookingReferenceRepository;
import com.ninos.repositories.BookingRepository;
import com.ninos.repositories.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final BookingReferenceRepository bookingReferenceRepository;

    @Value("${stripe.api.secret.key}")
    private String secretKey;

    /*
    we are initializing a payment, this will return a unique transactionId called secret which
    will be used to process, complete the payment in the frontend
    * */
    public Response initializePayment(PaymentRequest paymentRequest) {

        log.info("Creating payment intent for booking with reference: {}", paymentRequest.getBookingReference());
        Stripe.apiKey = secretKey;

        String bookingReference = paymentRequest.getBookingReference();

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new NotFoundException("Booking with reference " + bookingReference + " not found"));

        if (booking.getPaymentStatus() == PaymentStatus.COMPLETED){
            throw new NotFoundException("Payment for booking with reference " + bookingReference + " already completed");
        }

        if(booking.getTotalPrice().compareTo(paymentRequest.getAmount()) != 0){
            throw new NotFoundException("Payment amount does not tally. Please Contact out customer support agent");
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // amount cents
                    .setCurrency("usd")
                    .putMetadata("bookingReference",bookingReference)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            String uniqueTransactionId = intent.getClientSecret();

            return Response.builder()
                    .status(200)
                    .message("success")
                    .transactionId(uniqueTransactionId)
                    .build();

        }catch (Exception e){
            throw new RuntimeException("Error creating payment intent", e);
        }
    }


    public void updatePaymentBooking(PaymentRequest paymentRequest) {
        log.info("Updating payment for booking with reference: {}", paymentRequest.getBookingReference());

        String bookingReference = paymentRequest.getBookingReference();

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new NotFoundException("Booking with reference " + bookingReference + " not found"));

        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentGateway(PaymentGateway.STRIPE);
        payment.setAmount(paymentRequest.getAmount());
        payment.setTransactionId(paymentRequest.getTransactionId());
        payment.setPaymentStatus(paymentRequest.isSuccess() ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setBookingReference(bookingReference);
        payment.setUser(booking.getUser());

        if (!paymentRequest.isSuccess()) {
            payment.setFailureReason(paymentRequest.getFailureReason());
        }

        paymentRepository.save(payment);  // save payment to database

        // create and send notification
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(booking.getUser().getEmail())
                .type(NotificationType.EMAIL)
                .bookingReference(bookingReference)
                .build();

        if (paymentRequest.isSuccess()) {
            booking.setPaymentStatus(PaymentStatus.COMPLETED);
            bookingRepository.save(booking);  // update the booking
            notificationDTO.setSubject("Booking Payment Successful");
            notificationDTO.setBody("Your payment for booking with reference: " + bookingReference + " was successful");
            notificationService.sendEmail(notificationDTO);
        }else {
            booking.setPaymentStatus(PaymentStatus.FAILED);
            bookingRepository.save(booking);
            notificationDTO.setSubject("Booking Payment Failed");
            notificationDTO.setBody("Your payment for booking with reference: " + bookingReference + " failed with reason:: " + paymentRequest.getFailureReason());
            notificationService.sendEmail(notificationDTO);
        }

    }

}
