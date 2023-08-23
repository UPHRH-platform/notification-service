package org.upsmf.notification.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.upsmf.notification.exception.InvalidRequestException;
import org.upsmf.notification.model.EmailNotification;
import org.upsmf.notification.service.EmailService;
import org.upsmf.notification.util.MailUtil;
import reactor.core.publisher.Mono;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailUtil mailUtil;

    @Override
    public Mono<ResponseEntity> send(EmailNotification emailNotification) throws MailException, InvalidRequestException {
        // validate request
        validateRequest(emailNotification);
        // looping to send mail
        emailNotification.getRecipientEmail().stream().forEach(toEmail -> mailUtil.sendEmail(toEmail, emailNotification.getEmailSubject(), emailNotification.getEmailBody()));
        // send ok response
        return Mono.just(ResponseEntity.ok().build());
    }

    private void validateRequest(EmailNotification emailNotification) {
        if(emailNotification == null) {
            throw new InvalidRequestException("Invalid Request");
        }
        if(emailNotification.getEmailSubject() == null || emailNotification.getEmailSubject().isBlank()) {
            throw new InvalidRequestException("Invalid Email Subject");
        }
        if(emailNotification.getRecipientEmail() == null || emailNotification.getRecipientEmail().isEmpty()) {
            throw new InvalidRequestException("Invalid Email Recipients");
        }
        if(emailNotification.getEmailBody() == null || emailNotification.getEmailBody().isBlank()) {
            throw new InvalidRequestException("Invalid Email Body");
        }
    }
}
