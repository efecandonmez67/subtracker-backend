package com.efecandonmez.subtracker.notification;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/test-upcoming")
    public void testUpcoming() {
        notificationService.notifyUpcomingPayments();
    }

    @PostMapping("/test-rate-change")
    public void testRateChange() {
        notificationService.notifySignificantRateChanges();
    }
}