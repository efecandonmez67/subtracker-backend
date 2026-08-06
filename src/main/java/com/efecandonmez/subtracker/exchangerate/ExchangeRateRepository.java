package com.efecandonmez.subtracker.exchangerate;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, java.util.UUID> {
    Optional<ExchangeRate> findByBaseCurrencyAndTargetCurrency(String base, String target);
}