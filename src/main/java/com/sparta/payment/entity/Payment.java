package com.sparta.payment.entity;

import com.sparta.payment.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@Entity
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    private String merchantUid;
    private Long amount;
    @Enumerated(EnumType.STRING) private PaymentStatus status;

    public Payment(String merchantUid, Long amount) {
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }
    public void approve() {
        if (this.status != PaymentStatus.PENDING) throw new IllegalStateException("PENDING 상태만 승인 가능");
        this.status = PaymentStatus.APPROVED;
    }
    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
}
