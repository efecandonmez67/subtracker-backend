package com.efecandonmez.subtracker.subscription;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

public enum BillingCycle {
    MONTHLY(date -> date.plusMonths(1)),
    YEARLY(date -> date.plusYears(1));

    private final UnaryOperator<LocalDate> advancer;

    BillingCycle(UnaryOperator<LocalDate> advancer) {
        this.advancer = advancer;
    }

    public LocalDate apply(LocalDate date) {
        return advancer.apply(date);
    }
}