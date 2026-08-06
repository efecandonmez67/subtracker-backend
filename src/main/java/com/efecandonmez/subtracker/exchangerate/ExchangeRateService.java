package com.efecandonmez.subtracker.exchangerate;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository repository;
    private final FrankfurterClient client;

    private static final String BASE_CURRENCY = "TRY";
    private static final List<String> TRACKED_CURRENCIES = List.of("USD", "EUR");

    public ExchangeRateService(ExchangeRateRepository repository, FrankfurterClient client) {
        this.repository = repository;
        this.client = client;
    }

    public void refreshAllRates() {
        for (String currency : TRACKED_CURRENCIES) {
            BigDecimal newRate = client.fetchRate(currency, BASE_CURRENCY);

            repository.findByBaseCurrencyAndTargetCurrency(currency, BASE_CURRENCY)
                    .ifPresentOrElse(
                            existing -> existing.updateRate(newRate),
                            () -> repository.save(new ExchangeRate(currency, BASE_CURRENCY, newRate))
                    );
        }
    }

    public List<ExchangeRate> getSignificantChanges(BigDecimal thresholdPercent) {
        return repository.findAll().stream()
                .filter(rate -> rate.percentChange().abs().compareTo(thresholdPercent) >= 0)
                .toList();
    }
}