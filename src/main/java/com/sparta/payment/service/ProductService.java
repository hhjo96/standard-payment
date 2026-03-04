package com.sparta.payment.service;

import com.sparta.payment.dto.ProductDto;
import com.sparta.payment.entity.Product;
import com.sparta.payment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductDto.ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("상품이 없음"));
        return new ProductDto.ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

}
