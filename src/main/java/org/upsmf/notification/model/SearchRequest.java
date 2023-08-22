package org.upsmf.notification.model;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SearchRequest {

    private String userId;
    private int page;
    private int size;
    private Map<String, String> sort;

}
