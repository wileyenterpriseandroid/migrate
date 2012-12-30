package com.migrate.rest.schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.migrate.webdata.model.PersistentSchema;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.migrate.webdata.model.PropertyIndex;
import com.migrate.storage.impl.JsonHelper;

public class Put extends com.migrate.rest.schema.SchemaCommon {
	protected String schemaName = "com.migrate.test.rest.schema.Contact";

	private String getJsonSchema(Class cl) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonSchema schema = mapper.generateJsonSchema(cl);
		return schema.toString();
	}
	
	private List<PropertyIndex> createIndexList() {
		List<PropertyIndex> indexList = new ArrayList<PropertyIndex>();
		PropertyIndex index = new PropertyIndex();
		index.setIndexName(schemaName);
		index.setFieldName("lastname");
		indexList.add(index);
		index = new PropertyIndex();
		index.setIndexName(schemaName);
		index.setFieldName("phone");
		indexList.add(index);
		return indexList;
	}
	@Test
	public void create() throws IOException {
		try {		
			HttpHeaders header = new HttpHeaders();
			header.add("content-type", "application/json");
			String postUrl = URL;
			
			String jsonSchemaStr = getJsonSchema(Contact.class);
			PersistentSchema persistentSchema = new PersistentSchema();
			TypeReference<HashMap<String,Object>> typeRef  = new TypeReference<HashMap<String,Object> >() {};
			Map<String, Object> jsonSchema =
					JsonHelper.readValue(jsonSchemaStr.getBytes(), typeRef );
			persistentSchema.setJsonSchema(jsonSchema);
			persistentSchema.setIndexList(createIndexList());

			HttpEntity<PersistentSchema> requestEntity = new HttpEntity<PersistentSchema>(persistentSchema,header);
			ResponseEntity<String> response = restTemplate.exchange(postUrl,
					  HttpMethod.POST, requestEntity, String.class, schemaName);
			log.info(response.getBody());
			
		} catch (HttpClientErrorException e) {
			log.error(e.getResponseBodyAsString());
			throw e;
		}
	}
}
