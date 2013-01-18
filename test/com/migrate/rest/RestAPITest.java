package com.migrate.rest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.migrate.storage.impl.JsonHelper;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/spring/applicationContext-test.xml" })
public class RestAPITest {
	protected static org.apache.log4j.Logger log = Logger.getLogger(RestAPITest.class);
	protected static  final String MIGRATE_URL = "http://localhost:8080/migrate/test/classes/{type}/{id}";
	protected String type = "type1";
	protected String id = "id1";
	protected Map<String, Object> object = new HashMap<String, Object>();
	
	@Before 
	public void setup() {
		object.put("xyz", 123);
	}
	
	@Autowired
	@Qualifier(value = "restTemplate")
	protected RestTemplate restTemplate;

	@Test
	public void test() {}
	
	protected void delete(String type, String id) throws IOException {
		try {
			String deleteUrl = MIGRATE_URL;
			HttpEntity<String> requestEntity = new  HttpEntity<String>(new HttpHeaders());
			ResponseEntity<String> response = restTemplate.exchange(deleteUrl,
					  HttpMethod.DELETE, requestEntity, String.class, type, id);
			log.info(response.getBody());		
		} catch (HttpClientErrorException e) {
			log.error(e.getResponseBodyAsString());
			throw e;
		}
	}
	
	protected void create() throws IOException {
		try {		
			HttpHeaders header = new HttpHeaders();
			header.add("content-type", "application/json");
			String putUrl = MIGRATE_URL;
			HttpEntity<Map<String, Object>> requestEntity = new  HttpEntity<Map<String, Object>>(object,header);
			ResponseEntity<String> response = restTemplate.exchange(putUrl,
					  HttpMethod.POST, requestEntity, String.class, type, id);
			log.info(response.getBody());
			
		} catch (HttpClientErrorException e) {
			log.error(e.getResponseBodyAsString());
			throw e;
		}
	}

    protected void put(Map<String, Object> map) throws IOException {
		try {		
			HttpHeaders header = new HttpHeaders();
			header.add("content-type", "application/json");
			String putUrl = MIGRATE_URL;
			HttpEntity<Map<String, Object>> requestEntity = new  HttpEntity<Map<String, Object>>(map,header);
			ResponseEntity<String> response = restTemplate.exchange(putUrl,
					  HttpMethod.PUT, requestEntity, String.class, type, id);
			log.info(response.getBody());
			
		} catch (HttpClientErrorException e) {
			log.error(e.getResponseBodyAsString());
			throw e;
		}
	}

    protected Map<String, Object> get() throws IOException {
		try {
			TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object> >() {};
			String getUrl = MIGRATE_URL;
			HttpEntity<String> requestEntity = new HttpEntity<String>(new HttpHeaders());
			ResponseEntity<String> response = restTemplate.exchange(getUrl,
					  HttpMethod.GET, requestEntity, String.class, "type1", "id1");
			log.info(response.getBody());
			Map<String, Object> ret = JsonHelper.readValue(response.getBody().getBytes(), typeRef);
			return ret;

		} catch (HttpClientErrorException e) {
			throw e;
		}
	}
	
	protected void validate (Map<String, Object> m1, Map<String, Object>m2) {
		for ( Entry<String, Object> e : m1.entrySet()) {
			String key = e.getKey();
			log.info("*** key" + key);
			assertEquals(e.getValue(), m2.get(key));			
		}
	}
}
