package com.migrate.exception;

import java.io.IOException;
/**
 * @author Zane Pan
 */
public class StorageFailureException extends IOException {
	public StorageFailureException(Throwable e) {
		super(e);
	}
}
