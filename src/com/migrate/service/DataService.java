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
    private static final String NAMESPACE = "__data";
    private static org.apache.log4j.Logger log = Logger.getLogger(SchemaService.class);

    @Autowired
    @Qualifier(value = "objectStore")
    private ObjectStore store;

    @Autowired
    @Qualifier(value = "luceneIndexService")
    private LuceneIndexService luceneIndexService;

    public GenericMap getObject(String className, String id, String tenantId) throws IOException {
        return store.get(NAMESPACE, id, className, GenericMap.class, tenantId);
    }

    public void storeObject(GenericMap data, String tenantId) throws IOException {

        // TODO: perhaps should flag an error if was not this already?
        data.setWd_namespace(NAMESPACE);
        luceneIndexService.updateIndex(data, tenantId);
        store.update(data, tenantId);
    }

    public void deleteObject(String className, String id, String tenantId) throws IOException {
        deleteObject(className, id, false, 0L, tenantId);
    }

    public void softDeleteObject(String className, String id, long now, String tenantId) throws IOException {
        deleteObject(className, id, false, now, tenantId);
    }

    public void permanentDelete(String className, String id, String tenantId) throws IOException {
        deleteObject(className, id, true, 0L, tenantId);
    }

    public void deleteObject(String className, String id, boolean isPermanent, long now, String tenantId) throws IOException {
        // TODO: ok drop the index, but still keep the deleted object?
        if (isPermanent) {
            luceneIndexService.deleteIndex(className, id);
        }
        store.delete(NAMESPACE, id, isPermanent, now, tenantId);
    }

    public void createObject(GenericMap data, String tenantId) throws IOException {
        log.info("calling createObject");
        luceneIndexService.updateIndex(data, tenantId);
        data.setWd_namespace(NAMESPACE);
        store.create(data, tenantId);
    }

    public List<GenericMap> luceneSearch(String className, String queryStr) throws ParseException, IOException {
        return luceneIndexService.search(className, queryStr);
    }

    public List<GenericMap> find(String className, long time, int start, int numMatches, String tenantId) throws ParseException, IOException {
        return store.findChanged(NAMESPACE, className, time, start, numMatches, tenantId);
    }
}
