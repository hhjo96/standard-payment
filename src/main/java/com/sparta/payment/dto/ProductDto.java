package com.sparta.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ProductDto {

    @Data @AllArgsConstructor
    public static class ProductResponse {
        private Long id;
        private String name;
        private Long price;
    }
}
