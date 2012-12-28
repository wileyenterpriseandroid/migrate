package com.migrate.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.migrate.dataModel.Schema;
import com.migrate.storage.ObjectStore;

@Component("schemaService")
public class SchemaService {
	private static org.apache.log4j.Logger log = Logger.getLogger(SchemaService.class);
	private static String SCHEMA = "__schema";
	
	@Autowired
	@Qualifier(value = "objectStore")
	private ObjectStore store;
	
	public void updateSchema(Schema schema) throws IOException {
		schema.setBucket(SCHEMA);
		store.update(schema);
	}
	
	public Schema getSchema(String schemaName) throws IOException {
		return store.get(SCHEMA, schemaName, Schema.class);
	}
}
