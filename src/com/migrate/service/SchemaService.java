package com.migrate.service;

import java.io.IOException;

import com.migrate.webdata.model.PersistentSchema;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.migrate.storage.ObjectStore;

@Component("schemaService")
public class SchemaService {
	private static org.apache.log4j.Logger log = Logger.getLogger(SchemaService.class);
	private static String SCHEMA = "__schema";
	
	@Autowired
	@Qualifier(value = "objectStore")
	private ObjectStore store;
	
	public void updateSchema(PersistentSchema persistentSchema) throws IOException {
		persistentSchema.setWd_classname(PersistentSchema.class.getName());
		persistentSchema.setWd_namespace(SCHEMA);
		store.update(persistentSchema);
	}
	
	public PersistentSchema getSchema(String schemaName) throws IOException {
		return store.get(SCHEMA, schemaName, PersistentSchema.class.getName(), PersistentSchema.class);
	}

    public void deleteSchema(String schemaName) throws IOException {
   		store.delete(SCHEMA, schemaName);
   	}
}
