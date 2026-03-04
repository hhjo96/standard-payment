package com.sparta.payment.service;

import com.sparta.payment.dto.PaymentDto;
import com.sparta.payment.infra.PgClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private final PgClient pgClient;
    private final PaymentTransactionService txService;

    public PaymentDto.PaymentReadyResponse preparePayment(PaymentDto.OrderRequest request) {
        String merchantUid = "PAY-" + UUID.randomUUID().toString();
        Long calculatedAmount = 100L * request.getQuantity(); // 임시 로직
        txService.savePendingPayment(merchantUid, calculatedAmount);
        return new PaymentDto.PaymentReadyResponse(merchantUid, calculatedAmount, "스프링 부트 마스터 강의");
    }

    // 🚨 여기서 모든 검증이 이루어집니다 (프론트 Confirm, 웹훅 모두 이곳을 거침)
    public void verifyAndApprove(String impUid, String merchantUid) {
        if (txService.isAlreadyApproved(merchantUid)) return; // 1차 방어 (멱등성)

        PgClient.PgPaymentData pgData = pgClient.getPaymentData(impUid); // 🌐 I/O 발생

        if (!txService.getSavedAmount(merchantUid).equals(pgData.getAmount())) {
            throw new IllegalArgumentException("결제 금액 위변조 의심!"); // 2차 방어 (교차 검증)
        }
        txService.processApproval(merchantUid); // 검증 통과 시 DB 확정
    }
}