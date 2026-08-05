package com.efecandonmez.subtracker.subscription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Setter
    @Column(nullable = false, length = 3)
    private String currency;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;

    @Setter
    @Column(nullable = false)
    private LocalDate nextPaymentDate;

    @Setter
    private String category;

    @Setter
    @Column(nullable = false)
    private boolean active = true;

    public Subscription(UUID userId, String name, BigDecimal price, String currency,
                        BillingCycle billingCycle, LocalDate nextPaymentDate, String category) {
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.billingCycle = billingCycle;
        this.nextPaymentDate = nextPaymentDate;
        this.category = category;
    }

    public void advanceNextPaymentDate() {
        this.nextPaymentDate = billingCycle.apply(this.nextPaymentDate);
    }
}