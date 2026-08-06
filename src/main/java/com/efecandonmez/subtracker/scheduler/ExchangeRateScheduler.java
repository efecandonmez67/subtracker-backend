package com.efecandonmez.subtracker.scheduler;

import com.efecandonmez.subtracker.exchangerate.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateScheduler.class);

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateScheduler(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void refreshRates() {
        log.info("Refreshing exchange rates...");
        exchangeRateService.refreshAllRates();
        log.info("Exchange rates refreshed.");
    }
}