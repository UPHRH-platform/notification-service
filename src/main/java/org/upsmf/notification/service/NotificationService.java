package org.upsmf.notification.service;

import org.springframework.http.ResponseEntity;
import org.upsmf.notification.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<ResponseDto> sendNotification(NotificationRequest request);

    Flux<NotificationResponse> search(SearchRequest searchRequest);

    Mono<ResponseEntity> updateNotificationReadStatus(UpdateNotificationRequest updateNotificationRequest);
}

