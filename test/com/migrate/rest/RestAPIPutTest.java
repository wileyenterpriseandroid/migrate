package com.migrate.rest;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.migrate.rest.schema.Contact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;


public class RestAPIPutTest extends RestAPITest {
	
	@Before 
	public void createData() throws IOException {
		delete(type, id);
		create();
	}
		
	@Test
	public void putTest() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = get();
		map.put("xyz", 456);
		JsonSchema schema = mapper.generateJsonSchema(Contact.class);
		log.info("******** " + schema.toString());		
		object.put("xyz", 456);
		put(map);
		validate(object, map );
	}
}
