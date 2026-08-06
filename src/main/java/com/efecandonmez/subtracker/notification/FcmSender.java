package com.efecandonmez.subtracker.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FcmSender {

    private static final Logger log = LoggerFactory.getLogger(FcmSender.class);

    public void send(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isBlank()) {
            log.warn("FCM token boş, bildirim gönderilmedi");
            return;
        }

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM bildirimi gönderildi: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM bildirimi gönderilemedi: {}", e.getMessage());
        }
    }
}