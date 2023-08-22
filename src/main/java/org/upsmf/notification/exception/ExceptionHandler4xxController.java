package org.upsmf.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.upsmf.notification.model.ErrorResponseDto;

@ControllerAdvice
public class ExceptionHandler4xxController {

    /**
     * Exception handler for Invalid request exception
     * @param e
     * @return
     */
    @ExceptionHandler(value = InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleInvalidRequestException(InvalidRequestException e) {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
