package org.upsmf.notification.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(includeFieldNames = true)
public class NotificationRequest {

    private String title;
    private String body;
    private String deviceToken;
    private String userId;
}
