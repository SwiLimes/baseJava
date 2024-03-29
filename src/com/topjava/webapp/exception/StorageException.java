package com.topjava.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public StorageException(String message, String uuid, Throwable cause) {
        super(message, cause);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
