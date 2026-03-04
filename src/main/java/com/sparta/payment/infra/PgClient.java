package com.sparta.payment.infra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class PgClient {

    // application.yml에 세팅한 포트원 V2 API Secret 키를 가져옵니다.
    @Value("${portone.api.secret}")
    private String apiSecret;

    @Value("${portone.api.base-url}")
    private String baseUrl;

    @Data
    @AllArgsConstructor
    public static class PgPaymentData {
        private String impUid; // 포트원 결제 고유 번호 (transactionId)
        private Long amount;   // 실제 결제 승인된 금액
        private String status; // 결제 상태 (paid 등)
    }

    //포트원 서버에 확인요청을 보내고 응답 받아오기
    public PgPaymentData getPaymentData(String impUid) {
        log.info("포트원 결제 단건 조회 API 호출 중... (impUid: {})", impUid);
        // 실무: apiSecret을 사용하여 포트원 API(https://api.portone.io/payments/{paymentId})를 호출합니다.

        // 우리서버가 포트원 서버에 요청보낼 때 필요
        RestTemplate restTemplate = new RestTemplate();

        //포트원 서버에 요청보내기 위해 준비
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne" + apiSecret);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = baseUrl + "/payments/" + impUid;

        //포트원 서버에 요청 날리기
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            //포트원 응답 받아서 파싱
            Map<String, Object> body = response.getBody();
            if (body == null) {
                throw new RuntimeException("포트원 API 응답이 비어있습니다.");
            }

            //merchantUid는 우리가 만든 주문번호, transactionId는 포트원이 만든 번호
            String transactionId = (String) body.get("transactionId");


            Map<String, Object> amountObj = (Map<String, Object>) body.get("amount");
            Long total = amountObj != null
                    ? ((Number) amountObj.get("total")).longValue()
                    : 0L;

            String status = (String) body.get("status");

            log.info("포트원 응답 수신 완료! transactionId={}, amount={}, status={}",
                    transactionId, total, status);

            return new PgPaymentData(transactionId, total, status);

        } catch (HttpClientErrorException e) {
            log.error("포트원 API 호출 실패: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("포트원 결제 조회 실패: " + e.getStatusCode());
        }

            // 실습: 네트워크 지연 0.5초 가정을 위한 sleep
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        log.info("🌐 PG사 응답 도착 완료! (테스트용 데이터 반환)");
//
//        // 프론트엔드 실습 코드의 금액(100원)과 맞춘 가짜 정상 응답
//        return new PgPaymentData(impUid, 1000L, "paid");
    }
}