package com.efecandonmez.subtracker.notification;

import com.efecandonmez.subtracker.auth.User;
import com.efecandonmez.subtracker.auth.UserRepository;
import com.efecandonmez.subtracker.exchangerate.ExchangeRate;
import com.efecandonmez.subtracker.exchangerate.ExchangeRateService;
import com.efecandonmez.subtracker.subscription.Subscription;
import com.efecandonmez.subtracker.subscription.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ExchangeRateService exchangeRateService;
    private final FcmSender fcmSender;

    public NotificationService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository,
                               ExchangeRateService exchangeRateService,
                               FcmSender fcmSender) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.exchangeRateService = exchangeRateService;
        this.fcmSender = fcmSender;
    }

    public void notifyUpcomingPayments() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Subscription> dueSoon = subscriptionRepository.findByNextPaymentDateAndActiveTrue(tomorrow);

        for (Subscription sub : dueSoon) {
            userRepository.findById(sub.getUserId()).ifPresent(user ->
                    fcmSender.send(
                            user.getFcmToken(),
                            "Yaklaşan Ödeme",
                            sub.getName() + " için yarın " + sub.getPrice() + " " + sub.getCurrency() + " çekilecek."
                    )
            );
        }
    }

    public void notifySignificantRateChanges() {
        List<ExchangeRate> allRates = exchangeRateService.getAllRates();

        for (ExchangeRate rate : allRates) {
            List<Subscription> affected = subscriptionRepository.findByCurrencyAndActiveTrue(rate.getBaseCurrency());

            affected.stream()
                    .map(Subscription::getUserId)
                    .distinct()
                    .forEach(userId -> userRepository.findById(userId).ifPresent(user -> {
                        if (rate.percentChange().abs().doubleValue() >= user.getRateChangeThreshold()) {
                            String direction = rate.percentChange().compareTo(java.math.BigDecimal.ZERO) > 0 ? "arttı" : "azaldı";
                            String body = rate.getBaseCurrency() + "/" + rate.getTargetCurrency() + " kuru %"
                                    + rate.percentChange().abs() + " " + direction + ". Bu para biriminden aboneliğiniz etkilenebilir.";
                            fcmSender.send(user.getFcmToken(), "Döviz Kuru Değişimi", body);
                        }
                    }));
        }
    }
}