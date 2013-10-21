package com.migrate.rest;

import java.io.IOException;

public class VersionMismatchException extends IOException {

    private static final long serialVersionUID = -2569918372089349114L;

    public VersionMismatchException(String message, Throwable cause)  {
        super(message, cause);
    }

    public VersionMismatchException(String message)  {
        super(message);
    }
}
