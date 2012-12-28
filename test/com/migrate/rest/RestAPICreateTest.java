package com.migrate.rest;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestAPICreateTest extends RestAPITest {
	@Before
	public void ensureDelete() throws IOException {
		delete(type, id);
	}
	
	@Test
	public void createTest() throws IOException {
		create();
		Map<String, Object> map = get();
		validate(object, map );
	}
	
	@After
	public void tearDown() throws IOException {
		delete(type, id);
	}
}
