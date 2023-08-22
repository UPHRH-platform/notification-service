package org.upsmf.notification.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmailNotification {

    private List<String> recipientEmail;
    private String emailSubject;
    private String emailBody;

}
