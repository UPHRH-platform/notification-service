package org.upsmf.notification.service;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.upsmf.notification.exception.InvalidRequestException;
import org.upsmf.notification.model.EmailNotification;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<ResponseEntity> send(EmailNotification emailNotification) throws MailException, InvalidRequestException;
}
