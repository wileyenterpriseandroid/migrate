package com.migrate.exception;

import java.io.IOException;

public class MigrateServiceException extends IOException {
    private int code;

    public static final int VERSION_MISMATCH = 0;
    public static final int STORAGE_FAILURE = 1;
    public static final int DUPLICATE_KEY = 2;

    public MigrateServiceException(int code) {
        this.code = code;
    }

    public MigrateServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public MigrateServiceException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public MigrateServiceException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
