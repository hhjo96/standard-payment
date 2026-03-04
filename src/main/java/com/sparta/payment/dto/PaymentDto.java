package com.sparta.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class PaymentDto {
    @Data
    public static class OrderRequest {
        private Long productId;
        private Integer quantity;
    }
    @Data @AllArgsConstructor
    public static class PaymentReadyResponse {
        private String merchantUid;
        private Long amount;
        private String orderName;
    }
    @Data public static class ConfirmRequest {
        private String impUid;      // (V2 스펙의 transactionId)
        private String merchantUid; // (V2 스펙의 paymentId)
    }
}
