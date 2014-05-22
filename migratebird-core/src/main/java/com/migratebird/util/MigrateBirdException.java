package com.migratebird.util;

public class MigrateBirdException extends RuntimeException {

    public MigrateBirdException() {
        super();
    }

    public MigrateBirdException(String message, Throwable cause) {
        super(message, cause);
    }

    public MigrateBirdException(String message) {
        super(message);
    }

    public MigrateBirdException(Throwable cause) {
        super(cause);
    }

}
