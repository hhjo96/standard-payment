package com.sparta.payment;

import com.sparta.payment.entity.Product;
import com.sparta.payment.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class initData {
    private final ProductRepository productRepository;

    @PostConstruct
    @Transactional
    public void init() {

        Product product = new Product("스프링 부트 강의", 2, 2000L);
        productRepository.save(product);
    }
}
