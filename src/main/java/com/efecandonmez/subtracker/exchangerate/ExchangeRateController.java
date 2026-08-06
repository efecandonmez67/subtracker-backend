package com.efecandonmez.subtracker.exchangerate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService service;

    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @PostMapping("/refresh")
    public void refresh() {
        service.refreshAllRates();
    }

    @PostMapping("/significant-changes")
    public List<ExchangeRate> significantChanges() {
        return service.getSignificantChanges(BigDecimal.valueOf(1));
    }
}