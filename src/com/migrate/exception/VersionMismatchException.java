package com.migrate.exception;

import java.io.IOException;
/**
 * @author Zane Pan
 */
public class VersionMismatchException extends IOException {
	public VersionMismatchException(String msg) {
		super(msg);
	}
}
