package org.upsmf.notification.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "affiliation-push-notifications", createIndex = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PushNotification {

    @Id
    private String id;

    @Field(name = "request_id")
    private String requestId;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "title")
    private String title;

    @Field(name = "device_token")
    private String deviceToken;

    @Field(name = "text")
    private String text;

    @Field(name = "is_read")
    private Boolean read = false;

    @Field(name = "created_date")
    private String createdDate;

    @Field(name = "updated_date")
    private String updatedDate;

    @Field(name = "created_date_ts")
    private Long createdDateTS;

    @Field(name = "updated_date_ts")
    private Long updatedDateTS;
}
