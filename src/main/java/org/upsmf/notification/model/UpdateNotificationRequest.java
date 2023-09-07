package org.upsmf.notification.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpdateNotificationRequest {

    private String userId;
    private List<String> notificationIds;
    private Boolean status;


}
