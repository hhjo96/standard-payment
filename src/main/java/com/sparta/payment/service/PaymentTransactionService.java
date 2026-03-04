package com.sparta.payment.service;

import com.sparta.payment.entity.Payment;

import com.sparta.payment.enums.PaymentStatus;
import com.sparta.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {
    private final PaymentRepository repository;

    @Transactional
    public void savePendingPayment(String merchantUid, Long amount) {
        repository.save(new Payment(merchantUid, amount));
    }

    @Transactional(readOnly = true)
    public boolean isAlreadyApproved(String merchantUid) {
        return repository.findById(merchantUid).orElseThrow().getStatus() == PaymentStatus.APPROVED;
    }

    @Transactional(readOnly = true)
    public Long getSavedAmount(String merchantUid) {
        return repository.findById(merchantUid).orElseThrow().getAmount();
    }

    @Transactional
    public void processApproval(String merchantUid) {
        Payment payment = repository.findById(merchantUid).orElseThrow();
        if (payment.getStatus() == PaymentStatus.PENDING) {
            payment.approve(); // 최종 확정!
        }
    }
}
