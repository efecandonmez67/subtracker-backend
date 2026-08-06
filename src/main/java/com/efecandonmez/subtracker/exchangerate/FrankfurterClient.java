package com.efecandonmez.subtracker.exchangerate;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class FrankfurterClient {

    private final RestClient restClient = RestClient.create("https://api.frankfurter.dev");

    public BigDecimal fetchRate(String base, String target) {
        FrankfurterResponse response = restClient.get()
                .uri("/v1/latest?from={base}&to={target}", base, target)
                .retrieve()
                .body(FrankfurterResponse.class);

        if (response == null || response.rates() == null || !response.rates().containsKey(target)) {
            throw new IllegalStateException("Kur verisi alınamadı: " + base + "->" + target);
        }

        return response.rates().get(target);
    }

    private record FrankfurterResponse(String base, String date, Map<String, BigDecimal> rates) {}
}