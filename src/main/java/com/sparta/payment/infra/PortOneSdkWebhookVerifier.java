package com.sparta.payment.infra;


import io.portone.sdk.server.errors.WebhookVerificationException;
import io.portone.sdk.server.webhook.Webhook;
import io.portone.sdk.server.webhook.WebhookVerifier;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PortOneSdkWebhookVerifier {

    // application.yml에 세팅한 웹훅 전용 시크릿 키를 가져옵니다.
    @Value("${portone.webhook.secret}")
    private String webhookSecret;

    private WebhookVerifier webhookVerifier;

    @PostConstruct
    void init() {
        // 앱 실행 시 시크릿 키를 주입받아 공식 SDK의 검증기를 초기화합니다.
        this.webhookVerifier = new WebhookVerifier(webhookSecret);
        log.info("🛡️ [PortOneSdkWebhookVerifier] 공식 SDK WebhookVerifier 초기화 완료");
    }

    /**
     * 웹훅 메시지를 검증하고 파싱된 Webhook 객체를 반환합니다.
     * * @param msgBody      요청 본문 (JSON 문자열 원문 그대로)
     * @param msgId        webhook-id 헤더
     * @param msgSignature webhook-signature 헤더
     * @param msgTimestamp webhook-timestamp 헤더
     * @return 검증 및 역직렬화된 Webhook 객체
     * @throws WebhookVerificationException 시그니처 검증 실패 시 예외 발생 (해킹 의심)
     */
    public Webhook verify(String msgBody, String msgId, String msgSignature, String msgTimestamp)
            throws WebhookVerificationException {

        // SDK가 서명 검증(무결성)과 재전송 공격 방어(시간표 확인), JSON 파싱을 동시에 수행합니다.
        return webhookVerifier.verify(msgBody, msgId, msgSignature, msgTimestamp);
    }
}
