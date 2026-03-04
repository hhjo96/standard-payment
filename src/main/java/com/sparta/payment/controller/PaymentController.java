package com.sparta.payment.controller;

import com.sparta.payment.dto.PaymentDto;
import com.sparta.payment.service.PaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentFacade paymentFacade;

    @PostMapping("/ready")
    public ResponseEntity<PaymentDto.PaymentReadyResponse> readyPayment(@RequestBody PaymentDto.OrderRequest request) {
        return ResponseEntity.ok(paymentFacade.preparePayment(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody PaymentDto.ConfirmRequest request) {
        paymentFacade.verifyAndApprove(request.getImpUid(), request.getMerchantUid());
        return ResponseEntity.ok("결제 성공");
    }
}