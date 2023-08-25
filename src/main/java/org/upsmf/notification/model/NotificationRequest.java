package org.upsmf.notification.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(includeFieldNames = true)
public class NotificationRequest {

    private String title;
    private String body;
    private List<String> deviceToken;
    private String userId;
}
