package com.migrate.exception;

import java.io.IOException;

/**
 * @author Zane Pan
 */
public class DuplicationKeyException extends IOException {
	public DuplicationKeyException(String msg) {
		super(msg);
	}
}
