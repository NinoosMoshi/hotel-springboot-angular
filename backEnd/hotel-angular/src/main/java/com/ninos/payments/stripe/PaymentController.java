package com.ninos.payments.stripe;

import com.ninos.dtos.Response;
import com.ninos.payments.stripe.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/pay")
    public ResponseEntity<Response> initializePayment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.initializePayment(paymentRequest));
    }

    @PutMapping("/update")
    public void updatePaymentBooking(@RequestBody PaymentRequest paymentRequest) {
        paymentService.updatePaymentBooking(paymentRequest);
    }


}

