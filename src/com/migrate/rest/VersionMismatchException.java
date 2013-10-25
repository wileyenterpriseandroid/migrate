package com.migrate.rest;

import com.migrate.exception.MigrateServiceException;

public class VersionMismatchException extends MigrateServiceException {

    private static final long serialVersionUID = -2569918372089349114L;

    public VersionMismatchException(String message, Throwable cause)  {
        super(MigrateServiceException.VERSION_MISMATCH, message, cause);
    }

    public VersionMismatchException(String message)  {
        super(MigrateServiceException.VERSION_MISMATCH, message);
    }
}
