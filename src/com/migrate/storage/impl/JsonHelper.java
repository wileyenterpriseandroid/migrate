package com.migrate.storage.impl;

import java.io.IOException;

import org.apache.log4j.Logger;

// TODO: are these the right imports?
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Zane Pan
 */
public class JsonHelper {
	private static org.apache.log4j.Logger log = Logger.getLogger(JsonHelper.class);
	
	public static <T> T readValue(byte[] src, Class<T> valueType) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(src, valueType);
	}

	public static <T> T readValue(byte[] src, TypeReference<?> valueTypeRef) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(src, valueTypeRef);
	}
	
	public static byte[] writeValueAsByte(Object value) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsBytes(value);
	}
}
