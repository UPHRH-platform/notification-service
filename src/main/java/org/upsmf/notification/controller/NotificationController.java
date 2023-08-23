package org.upsmf.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.upsmf.notification.model.NotificationRequest;
import org.upsmf.notification.model.NotificationResponse;
import org.upsmf.notification.model.ResponseDto;
import org.upsmf.notification.model.SearchRequest;
import org.upsmf.notification.service.NotificationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * API to send out Push Notification using FCM
     *
     * @param request
     * @return
     */
    @PostMapping("/send")
    public Mono<ResponseDto> sendNotification(@RequestBody NotificationRequest request){
       return notificationService.sendNotification(request);
    }

    @PostMapping("/all")
    public Flux<NotificationResponse> getNotificationByPage(@RequestBody SearchRequest searchRequest) {
        return notificationService.search(searchRequest);
    }
}
