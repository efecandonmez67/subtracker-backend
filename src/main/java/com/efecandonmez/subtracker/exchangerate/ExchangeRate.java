package com.efecandonmez.subtracker.exchangerate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exchange_rates", uniqueConstraints = @UniqueConstraint(columnNames = {"baseCurrency", "targetCurrency"}))
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 3)
    private String baseCurrency;

    @Column(nullable = false, length = 3)
    private String targetCurrency;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal rate;

    @Column(nullable = false)
    private BigDecimal previousRate;

    @Column(nullable = false)
    private Instant fetchedAt;

    public ExchangeRate(String baseCurrency, String targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.previousRate = rate;
        this.fetchedAt = Instant.now();
    }

    public void updateRate(BigDecimal newRate) {
        this.previousRate = this.rate;
        this.rate = newRate;
        this.fetchedAt = Instant.now();
    }

    public BigDecimal percentChange() {
        if (previousRate.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return rate.subtract(previousRate)
                .divide(previousRate, 6, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}