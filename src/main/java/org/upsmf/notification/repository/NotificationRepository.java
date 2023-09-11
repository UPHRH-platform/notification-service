package org.upsmf.notification.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.upsmf.notification.entity.PushNotification;

import java.util.List;

@Repository
public interface NotificationRepository extends ElasticsearchRepository<PushNotification, String> {

    @Query("{\"bool\":{\"must\":[{\"match\":{\"user_id\":\"?0\"}}]}}")
    Iterable<PushNotification> findAllByUserId(String userId);
}
