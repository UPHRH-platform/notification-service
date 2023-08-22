package org.upsmf.notification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDto {

    private int code;
    private String message;

    public ErrorResponseDto(String message)
    {
        super();
        this.message = message;
    }

    public ErrorResponseDto(int code, String message)
    {
        super();
        this.code = code;
        this.message = message;
    }
}