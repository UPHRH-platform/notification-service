package org.upsmf.notification.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.upsmf.notification.entity.PushNotification;

@Repository
public interface NotificationRepository extends ElasticsearchRepository<PushNotification, String> {
}
