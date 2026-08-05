package com.efecandonmez.subtracker.subscription.dto;

import com.efecandonmez.subtracker.subscription.BillingCycle;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        String name,
        BigDecimal price,
        String currency,
        BillingCycle billingCycle,
        LocalDate nextPaymentDate,
        String category,
        boolean active
) {}