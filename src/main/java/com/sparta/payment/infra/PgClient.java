package com.sparta.payment.infra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PgClient {

    // application.yml에 세팅한 포트원 V2 API Secret 키를 가져옵니다.
    @Value("${portone.api.secret}")
    private String apiSecret;

    @Data
    @AllArgsConstructor
    public static class PgPaymentData {
        private String impUid; // 포트원 결제 고유 번호 (transactionId)
        private Long amount;   // 실제 결제 승인된 금액
        private String status; // 결제 상태 (paid 등)
    }

    /**
     * 포트원 결제 단건 조회 API 호출 (가상)
     * 주의: 이 메서드는 네트워크 I/O가 발생하므로 절대 @Transactional 안에서 호출하면 안 됩니다!
     */
    public PgPaymentData getPaymentData(String impUid) {
        log.info("🌐 포트원 결제 단건 조회 API 호출 중... (impUid: {})", impUid);
        // 실무: apiSecret을 사용하여 포트원 API(https://api.portone.io/payments/{paymentId})를 호출합니다.

        // 실습: 네트워크 지연 0.5초 가정을 위한 sleep
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("🌐 PG사 응답 도착 완료! (테스트용 데이터 반환)");

        // 프론트엔드 실습 코드의 금액(100원)과 맞춘 가짜 정상 응답
        return new PgPaymentData(impUid, 100L, "paid");
    }
}