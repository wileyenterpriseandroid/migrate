package com.migrate.storage;

import java.io.IOException;
import java.util.List;


import com.migrate.webdata.model.GenericMap;
import com.migrate.webdata.model.PersistentObject;

/**
 * @author Zane Pan
 */
public interface ObjectStore {
	<T extends PersistentObject> T get(String namespace, String key, String className, Class<T> valueType, String tenantId)
            throws IOException;

	void update(PersistentObject bo, String tenantId) throws IOException;

	void create(PersistentObject bo, String tenantId) throws IOException;

	void delete(String namespace, String key, String tenantId) throws IOException;

	void delete(String namespace, String key, boolean isPermanent, long now, String tenantId) throws IOException;

	List<GenericMap> findChanged(String namespace, String classname, long time, int start, int numMatches, String tenantId)
            throws IOException;
}
