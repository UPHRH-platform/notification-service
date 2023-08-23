package org.upsmf.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.upsmf.notification.exception.InvalidRequestException;
import org.upsmf.notification.model.EmailNotification;
import org.upsmf.notification.model.ErrorResponseDto;
import org.upsmf.notification.model.ResponseDto;
import org.upsmf.notification.service.EmailService;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/email")
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
        try {
            emailServiceImpl.send(emailNotification);
            return Mono.just(ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), "Success")));
        } catch (Exception e) {
            return Mono.just(createErrorResponse(e));
        }
    }

    /**
     * Generic error
     * @param e
     * @return
     */
    private ResponseEntity createErrorResponse(Exception e) {
        if(e instanceof InvalidRequestException) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage()));
        }
        return ResponseEntity.internalServerError().body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage()));
    }
}
