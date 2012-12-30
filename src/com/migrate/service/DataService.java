/**
 * @author Zane Pan
 */
package com.migrate.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.migrate.webdata.model.GenericMap;
import com.migrate.storage.ObjectStore;

@Component("dataService")
public class DataService {
	private static org.apache.log4j.Logger log = Logger.getLogger(SchemaService.class);
	@Autowired
	@Qualifier(value = "objectStore")
	private ObjectStore store;

	@Autowired
	@Qualifier(value = "luceneIndexService")
	private LuceneIndexService luceneIndexService;

	public GenericMap getObject(String className, String id) throws IOException {
		return store.get(className, id, GenericMap.class);	
	}
	
	public void storeObject(GenericMap data) throws IOException {
		luceneIndexService.updateIndex(data);
		store.update(data);
	}
	
	public void deleteObject(String className, String id) throws IOException {
		luceneIndexService.deleteIndex(className, id);
		store.delete(className,  id);
	}
	
	public void createObject(GenericMap data) throws IOException {
		store.create(data);
	}
	
	public List<GenericMap> find(String className, String queryStr) throws ParseException, IOException {
		return luceneIndexService.search(className, queryStr);
	}
}
