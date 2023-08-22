package org.upsmf.notification.model;

import lombok.*;
import org.upsmf.notification.entity.PushNotification;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotificationResponse {
    private Long count;
    private List<PushNotification> data;
}
