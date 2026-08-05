package com.efecandonmez.subtracker.subscription.dto;

import com.efecandonmez.subtracker.subscription.BillingCycle;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionRequest(
        @NotBlank String name,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull BillingCycle billingCycle,
        @NotNull @FutureOrPresent LocalDate nextPaymentDate,
        String category
) {}