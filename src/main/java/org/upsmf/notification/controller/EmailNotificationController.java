package org.upsmf.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.upsmf.notification.model.EmailNotification;
import org.upsmf.notification.service.EmailService;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/email")
public class EmailNotificationController {

    @Autowired
    private EmailService emailServiceImpl;

    /**
     * API to send out email notification
     * @param emailNotification
     * @return Mono<ResponseEntity>
     */
    @PostMapping("/notify")
    public Mono<ResponseEntity> sendEmail(@RequestBody EmailNotification emailNotification) {
        return emailServiceImpl.send(emailNotification);
    }

    /**
     * Generic error
     * @param message
     * @return
     */
    private ResponseEntity createErrorResponse(String message) {
        return ResponseEntity.internalServerError().body(message);
    }
}
