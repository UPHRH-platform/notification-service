package org.upsmf.notification.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.upsmf.notification.config.EsConfig;
import org.upsmf.notification.entity.PushNotification;
import org.upsmf.notification.model.NotificationRequest;
import org.upsmf.notification.model.NotificationResponse;
import org.upsmf.notification.model.ResponseDto;
import org.upsmf.notification.model.SearchRequest;
import org.upsmf.notification.repository.NotificationRepository;
import org.upsmf.notification.service.NotificationService;
import org.upsmf.notification.util.DateUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Value("${fcm.service.account.file.path}")
    private String fcmServiceAccountPath;

    @Autowired
    private EsConfig esConfig;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostConstruct
    public void init() throws IOException {
        initializeFCM();
    }
    public void initializeFCM() throws IOException {
        // Initialize FirebaseApp with Firebase Admin SDK configuration
        FileInputStream fileInputStream = null;
        try {
            URL fcmFile = getClass().getClassLoader().getResource(fcmServiceAccountPath);
            fileInputStream = new FileInputStream(new File(fcmFile.toURI()));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public Mono<ResponseDto> sendNotification(NotificationRequest request) {
        // validate request
        validateNotificationPayload(request);
        // Create a new Firebase Cloud Messaging (FCM) message
        for(String token : request.getDeviceToken()) {
            Message message = Message.builder()
                    .putData("title", request.getTitle())
                    .putData("body", request.getBody())
                    .setToken(token)
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                saveNotificationInES(request, token, response);
            } catch (FirebaseMessagingException e) {
                log.error("Error in sending push", e);
            } catch (Exception e) {
                log.error("Error in push", e);
            }
        }
        return Mono.just(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.name()));
    }

    private void validateNotificationPayload(NotificationRequest request) {
        if(request == null) {
            throw new RuntimeException("Invalid Request");
        }
        if(request.getUserId() == null || request.getUserId().isBlank()) {
            throw new RuntimeException("Invalid User ID");
        }
        if(request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Invalid Message Title");
        }
        if(request.getBody() == null || request.getBody().isBlank()) {
            throw new RuntimeException("Invalid Message Body");
        }
        if(request.getDeviceToken() == null || request.getDeviceToken().isEmpty()) {
            throw new RuntimeException("Invalid Tokens");
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws Exception
     */
    private void saveNotificationInES(NotificationRequest request, String token, String response) throws Exception {
        if(response != null && !response.isBlank()){
            String requestId = response.substring(response.lastIndexOf("/")+1);
            Timestamp currentTimestamp = new Timestamp(DateUtil.getCurrentDate().getTime());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATE_FORMAT);
            PushNotification pushNotification = PushNotification.builder()
                    .read(false)
                    .text(request.getBody())
                    .title(request.getTitle())
                    .deviceToken(token)
                    .requestId(requestId)
                    .userId(request.getUserId())
                    .createdDate(currentTimestamp.toLocalDateTime().format(dateTimeFormatter))
                    .createdDateTS(currentTimestamp.getTime())
                    .updatedDate(currentTimestamp.toLocalDateTime().format(dateTimeFormatter))
                    .updatedDateTS(currentTimestamp.getTime())
                    .build();
            notificationRepository.save(pushNotification);
        }
    }

    /**
     * API to get notification by users
     * @param searchRequest
     * @return
     */
    @Override
    public Flux<NotificationResponse> search(SearchRequest searchRequest) {
        SearchResponse searchResponse;
        // get all key values
        searchResponse = searchNotifications(searchRequest);
        if(searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == null || searchResponse.getHits().getTotalHits().value == 0) {
            return Flux.just(NotificationResponse.builder().count(0l).data(Collections.EMPTY_LIST).build());
        }
        List<PushNotification> documents = extractPushNotifications(searchResponse);
        // create response
        NotificationResponse response = NotificationResponse.builder().count(searchResponse.getHits().getTotalHits().value).data(documents).build();
        // send response
        return Flux.just(response);
    }

    /**
     *
     * @param searchResponse
     * @return
     */
    private static List<PushNotification> extractPushNotifications(SearchResponse searchResponse) {
        List<PushNotification> documents = new ArrayList<PushNotification>();
        for (SearchHit hit : searchResponse.getHits()) {
            PushNotification pushNotification = new PushNotification();
            pushNotification.setId(hit.getId());
            for (Map.Entry entry : hit.getSourceAsMap().entrySet()) {
                String key = (String) entry.getKey();
                setValues(entry, key, pushNotification);
            }
            documents.add(pushNotification);
        }
        return documents;
    }

    /**
     *
     * @param entry
     * @param key
     * @param pushNotification
     */
    private static void setValues(Map.Entry<String, Object> entry, String key, PushNotification pushNotification) {
        switch (key) {
            case "request_id":
                pushNotification.setRequestId((String) entry.getValue());
                break;
            case "user_id":
                pushNotification.setUserId((String) entry.getValue());
                break;
            case "title":
                pushNotification.setTitle((String) entry.getValue());
                break;
            case "device_token":
                pushNotification.setDeviceToken((String) entry.getValue());
                break;
            case "text":
                pushNotification.setText((String) entry.getValue());
                break;
            case "is_read":
                pushNotification.setRead((Boolean) entry.getValue());
                break;
            case "created_date":
                pushNotification.setCreatedDate((String) entry.getValue());
                break;
            case "updated_date":
                pushNotification.setUpdatedDate((String) entry.getValue());
                break;
            case "created_date_ts":
                pushNotification.setCreatedDateTS((Long) entry.getValue());
                break;
            case "updated_date_ts":
                pushNotification.setUpdatedDateTS((Long) entry.getValue());
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param searchRequest
     * @return
     */
    private SearchResponse searchNotifications(SearchRequest searchRequest) {
        SearchResponse searchResponse;
        String keyValue = searchRequest.getSort().keySet().iterator().next();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(createTicketSearchQuery(searchRequest))
                .from(searchRequest.getPage())
                .size(searchRequest.getSize())
                .sort(keyValue, SortOrder.valueOf(searchRequest.getSort().get(searchRequest.getSort().keySet().iterator().next()).toUpperCase()));

        org.elasticsearch.action.search.SearchRequest search = new org.elasticsearch.action.search.SearchRequest("affiliation-push-notifications");
        search.searchType(SearchType.QUERY_THEN_FETCH);
        search.source(searchSourceBuilder);
        try {
            searchResponse = esConfig.elasticsearchClient().search(search, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return searchResponse;
    }

    /**
     *
     * @param searchRequest
     * @return
     */
    private BoolQueryBuilder createTicketSearchQuery(SearchRequest searchRequest) {
        BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();
        // search by keyword
        if (searchRequest.getUserId() != null && !searchRequest.getUserId().isBlank()) {
            MatchQueryBuilder ccMatchQuery = QueryBuilders.matchQuery("user_id", searchRequest.getUserId());
            BoolQueryBuilder ccSearchQuery = QueryBuilders.boolQuery();
            ccSearchQuery.must(ccMatchQuery);
            finalQuery.must(ccSearchQuery);
        }
        return finalQuery;
    }
}
