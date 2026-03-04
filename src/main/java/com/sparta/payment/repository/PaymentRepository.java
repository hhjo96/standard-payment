package com.sparta.payment.repository;

import com.sparta.payment.entity.Payment;
import com.sparta.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findAllByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime time);

    @Modifying
    @Query("update Payment p set p.status = :after where p.status = :before and p.createdAt < :time")
    int bulkUpdate(@Param("before") PaymentStatus before, @Param("after") PaymentStatus after, @Param("time") LocalDateTime time);
}

