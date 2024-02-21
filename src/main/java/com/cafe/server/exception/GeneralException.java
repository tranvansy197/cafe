package com.cafe.server.exception;

public class GeneralException extends RuntimeException{
    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(Throwable cause) {
        super(cause);
    }
}
