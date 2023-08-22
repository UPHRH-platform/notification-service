package org.upsmf.notification.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {

    private int code;
    private String message;

    public ResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseDto(String message) {
        this.message = message;
    }
}
