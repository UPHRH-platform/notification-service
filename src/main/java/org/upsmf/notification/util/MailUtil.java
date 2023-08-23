package org.upsmf.notification.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Component
public class MailUtil {

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttls;

    @Value("${mail.content.type}")
    private String contentType;

    /**
     * Method to send mail
     * @param toEmail
     * @param mailSubject
     * @param mailBody
     */
    public void sendEmail(String toEmail, String mailSubject, String mailBody) {
        // get properties
        Properties props = getProperties();
        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });
        // send mail
        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(userName));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            // Set Subject: header field
            message.setSubject(mailSubject);
            // Send the actual HTML message, as big as you like
            message.setContent(mailBody, contentType);
            // Send message
            Transport.send(message);
            log.info("Sent message successfully....");
        } catch (MessagingException e) {
            log.error("Error while sending email", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * method to set properties
     * @return
     */
    private Properties getProperties() {
        // set properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        return props;
    }

}
