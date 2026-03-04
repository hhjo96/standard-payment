package com.sparta.payment.controller;

import com.sparta.payment.infra.PortOneSdkWebhookVerifier;
import com.sparta.payment.service.PaymentFacade;
import io.portone.sdk.server.webhook.Webhook;
import io.portone.sdk.server.webhook.WebhookTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebhookController {
    private final PortOneSdkWebhookVerifier verifier;
    private final PaymentFacade paymentFacade;

    @PostMapping(value = "/portone-webhook", consumes = "application/json")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String rawBody,
            @RequestHeader("webhook-id") String webhookId,
            @RequestHeader("webhook-timestamp") String webhookTimestamp,
            @RequestHeader("webhook-signature") String webhookSignature) {

        try {
            Webhook webhook = verifier.verify(rawBody, webhookId, webhookSignature, webhookTimestamp);
            if (webhook instanceof WebhookTransaction transaction) {
                // 검증 성공 시, 프론트엔드 Confirm과 동일한 Facade 메서드를 호출!
                paymentFacade.verifyAndApprove(
                        transaction.getData().getTransactionId(),
                        transaction.getData().getPaymentId()
                );
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.warn("웹훅 처리 실패: {}", e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }
}
