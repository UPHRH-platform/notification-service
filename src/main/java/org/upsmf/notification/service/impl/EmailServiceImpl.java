package org.upsmf.notification.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.upsmf.notification.exception.InvalidRequestException;
import org.upsmf.notification.model.EmailNotification;
import org.upsmf.notification.service.EmailService;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public Mono<ResponseEntity> send(EmailNotification emailNotification) throws MailException {
        // validate request
        validateRequest(emailNotification);
        // create request
        SimpleMailMessage simpleMailMessage = createEmailRequestBody(emailNotification);
        // send email
        javaMailSender.send(simpleMailMessage);
        // send ok response
        return Mono.just(ResponseEntity.ok().build());
    }

    private SimpleMailMessage createEmailRequestBody(EmailNotification emailNotification) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // set request
        String toEmails = String.join(",", emailNotification.getRecipientEmail());
        // sender email
        simpleMailMessage.setFrom(senderEmail);
        // recipient emails
        simpleMailMessage.setTo(toEmails);
        // sent date
        simpleMailMessage.setSentDate(new Date());
        // subject
        simpleMailMessage.setSubject(emailNotification.getEmailSubject());
        // body
        simpleMailMessage.setText(emailNotification.getEmailBody());
        return simpleMailMessage;
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
