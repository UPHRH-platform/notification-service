package org.upsmf.notification.service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;
import org.upsmf.notification.entity.NotificationRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;

@Service
public class NotificationService {

    public NotificationService() {
        // Initialize FirebaseApp with Firebase Admin SDK configuration
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(getClass()
                            .getResourceAsStream("/service-account.json")))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Mono<String> sendNotification(NotificationRequest request) {
        // Create a new Firebase Cloud Messaging (FCM) message
        Message message = Message.builder()
                .putData("title", request.getTitle())
                .putData("body", request.getBody())
                .setToken("52e7e3c021ec147e5b86cb07e935ffbf")
                .build();
        // Send the message and return a Mono representing the result
        return Mono.create(callback -> {
            try {
                String response = FirebaseMessaging.getInstance().send(message);
                callback.success(response);
            } catch (FirebaseMessagingException e) {
                callback.error(e);
            }
        });
    }


   /* public Flux<String> sendNotificationToAll(NotificationRequest request){

    }*/
}
