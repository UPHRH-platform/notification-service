package org.upsmf.notification.exception;

public class InvalidRequestException extends RuntimeException {

    private String message;

    public InvalidRequestException() {
    }

    public InvalidRequestException(String msg) {
        super(msg);
        this.message = msg;
    }
}

