package com.migrate.exception;

import java.io.IOException;
/**
 * @author Zane Pan
 */
public class StorageFailureException extends MigrateServiceException {
	public StorageFailureException(Throwable e) {
		super(MigrateServiceException.STORAGE_FAILURE, e);
	}
}
