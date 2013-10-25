package com.migrate.exception;

/**
 * @author Zane Pan
 */
public class VersionMismatchException extends MigrateServiceException {
	public VersionMismatchException(String msg) {
		super(MigrateServiceException.VERSION_MISMATCH, msg);
	}
}
