package com.migrate.rest.schema;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.migrate.rest.schema.Put;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/spring/applicationContext-test.xml" })
public class SchemaCommon {
	protected static org.apache.log4j.Logger log = Logger.getLogger(Put.class);
	protected static  final String URL = "http://migrate.eordercenter.com/test/schema/{schemaName}";

	@Autowired
	@Qualifier(value = "restTemplate")
	protected RestTemplate restTemplate;
}
