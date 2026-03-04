package com.sparta.payment.scheduler;

import com.sparta.payment.enums.PaymentStatus;
import com.sparta.payment.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCleanScheduler {

    private final PaymentRepository paymentRepository;

    @Value("${payment.clean.pending-expire-minutes}")
    private int pendingExpireMinutes;

    @Scheduled(fixedRateString = "${payment.clean.scheduler-rate-ms}")
    @Transactional
    public void expireStalePendingPayments() {

        LocalDateTime time = LocalDateTime.now().minusMinutes(pendingExpireMinutes);

        log.info("[PaymentCleanScheduler] 실행 시각: {}, 기준 시각: {}", LocalDateTime.now(), time);

        int updatedCount = paymentRepository.bulkUpdate(
                PaymentStatus.PENDING,
                PaymentStatus.FAILED,
                time
        );
        log.info("[PaymentCleanScheduler] FAILED 처리 건수: {}건", updatedCount);
    }
}
