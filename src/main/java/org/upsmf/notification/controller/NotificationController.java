package org.upsmf.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.upsmf.notification.entity.NotificationRequest;
import org.upsmf.notification.service.NotificationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-notification")
    private Mono<String> sendNotification(@RequestBody NotificationRequest request){
       return notificationService.sendNotification(request);
    }

  /*  @PostMapping("/send-to-all")
    public Flux<String> sendNotificationToAll(@RequestBody NotificationRequest request) {
        return notificationService.sendNotificationToAll(request);
    }*/



}
