package com.cafe.server.exception;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}
