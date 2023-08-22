package org.upsmf.notification.service;

import org.upsmf.notification.model.NotificationRequest;
import org.upsmf.notification.model.NotificationResponse;
import org.upsmf.notification.model.ResponseDto;
import org.upsmf.notification.model.SearchRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<ResponseDto> sendNotification(NotificationRequest request);

    Flux<NotificationResponse> search(SearchRequest searchRequest);
}
