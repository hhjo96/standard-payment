package com.sparta.payment.repository;

import com.sparta.payment.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long> {
}
