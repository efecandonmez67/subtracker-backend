package com.efecandonmez.subtracker.scheduler;

import com.efecandonmez.subtracker.notification.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentReminderScheduler {

    private final NotificationService notificationService;

    public PaymentReminderScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 9 * * *") // her gün 09:00
    public void remindUpcomingPayments() {
        notificationService.notifyUpcomingPayments();
    }
}